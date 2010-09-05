;; This file is part of Cryptovide.
;;
;;     Cryptovide is free software: you can redistribute it and/or modify
;;     it under the terms of the GNU General Public License as published by;;
;;     the Free Software Foundation, either version 3 of the License, or
;;     (at your option) any later version.
;;
;;     Cryptovide is distributed in the hope that it will be useful,
;;     but WITHOUT ANY WARRANTY; without even the implied warranty of
;;     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
;;     GNU General Public License for more details.
;;
;;     You should have received a copy of the GNU General Public License
;;     along with Cryptovide.  If not, see <http://www.gnu.org/licenses/>.
;;
;;     copyright Arthur Ulfeldt 2009, 2010

(ns 
 #^{:author "Arthur Ulfeldt", 
       :doc "miscelanios libs"}
 com.cryptovide.misc
 (:gen-class)
 (:import java.security.SecureRandom)
 (:use clojure.contrib.math
       com.cryptovide.modmath
       clojure.contrib.logging
       [clojure.contrib.duck-streams :only (reader writer)]))

(def default-block-size 8) ;bits

(defstruct secret  :index :block-size :data :padding)

(defn byte-to-int [byte]
  (if (neg? byte)
    (+ byte 256)
    byte))

(defn bytes-to-number [bytes] 
  "Converts a LEAST SIGNIFICANT BYTE FIRST sequence of BYTES to a whole Number"
  (let [powers (iterate #(* % 256) 1)
        unsigned-bytes (map byte-to-int bytes)]
    (reduce + 0 (map * unsigned-bytes powers))))

(defn number-to-bytes
  "Converts a posative whole number to a LEAST SIGNIFICANT BYTE FIRST sequence of bytes"
  ([inty]
     (if (= inty 0)
       [(byte 0)]
       (number-to-bytes inty [])))
  ([inty bytes]
     (if (= 0 inty)
       bytes
       (recur (quot inty 256) (conj bytes (byte (rem inty 256)))))))

(defn intify [stringy]
  (map int (seq stringy)))

(defn byte-seq [rdr]
  "create a lazy seq of bytes in a file and close the file at the end"
  (let [result (. rdr read)]
    (if (= result -1)
      (do (. rdr close) nil)
      (lazy-seq (cons result (byte-seq rdr))))))

(defn ones [n]
  "create a binary number with this many ones"
  (- (bit-set 0 n) 1))

(defn extract-bits
  [bytes len offset]
  (bit-shift-right (bit-and bytes (bit-shift-left (ones len) offset)) offset))

(defn bit-shift-right-without-java-bugs [x n]
  "some jvms think that 1>>32*k = 1 (for all values of k)"
  (if (= 0 (rem n 32))
    0
    (bit-shift-right x n)))

; I dont like passing in a reference to collect the ammount of 
; padding that was/will-be added to the last block of the sequence.
(defn block-seq
  "converts a seq from in-block-size to out-block-size"
  ([in-block-size out-block-size bytes]
     (block-seq in-block-size out-block-size bytes (ref 0)))
  ([in-block-size out-block-size bytes padding-ref]
     (letfn [(make-seq 
              ([in-block-size out-block-size bytes bits length padding-ref]
                                        ;              (println "bits:"bits "length:"length)
                 (if (>= length out-block-size)
                   (lazy-seq
                     (cons
                      (extract-bits bits out-block-size 0)
                      (make-seq in-block-size out-block-size bytes
                                (bit-shift-right-without-java-bugs bits out-block-size)
                                (- length out-block-size) padding-ref)))
                   (dosync
                    (let [some-bits  (first bytes)
                          more-bytes (rest bytes)]
                      (if (nil? some-bits) ;when we cant get more bits
                        (if (= length 0) 
                          nil         ;end the seq if no leftover bits
                          (do
                            (commute padding-ref + (- out-block-size length)) ;save the padding
                            (cons bits nil))) ; pad the partial block at the end
                        (make-seq in-block-size out-block-size more-bytes
                                  (bit-or bits (bit-shift-left some-bits length))
                                  (+ length in-block-size) padding-ref)))))))]
       (make-seq in-block-size out-block-size bytes 0 0 padding-ref))))


(defn queue
  ([] clojure.lang.PersistentQueue/EMPTY)
  ([& args]
  (into (queue) args)))

(defn butlast-with-callback
  "everything up to the n'th element from the end, then evaluates the 
callback function on the last elements."
  ([col n callback]
     (letfn [(helper [col n callback buffer]
		     (if (empty? col) 
		       (do 
			 (callback buffer)
			 nil)
		       (lazy-seq
			 (cons
			  (first buffer)
			  (helper
			   (rest col) n callback (conj (pop buffer) (first col)))))))]
       (let [[tmp rest-of-col] (split-at n col)
	     buffer (into  (queue) tmp)]
	 (helper rest-of-col n callback buffer)))))
      
(defn write-block-seq
  "writes a sequence of blocks to a file and appends the trailer"
  [file blocks block-size padding-ref]
    (dorun
     (lazy-cat
      (map #(. file (write (int %)))
	   (block-seq block-size 8 blocks padding-ref))
      (map #(. file (write %))
	   (block-seq 32 8 (list (int @padding-ref)) (ref 0))))))

(defn read-block-seq
  "reads a sequence of blocks from a file and adds padding"
  [rdr block-size padding-ref]
  (block-seq 8 block-size 
	     (butlast-with-callback
	      (byte-seq rdr) ; this will close rdr
	      4
	      #(dosync (commute padding-ref + (bytes-to-number %))))
             padding-ref))

(defn write-seq-to-file [file-name & data]
  "writes sequences of things that can be cast to ints, to the open file"
  (with-open [file (writer file-name)]
    (when-not (empty? data)
      (doall (map #(. file (write %))
		  (if (seq? (first data))
		    (map int (first data))
		    (list (int (first data))))))
      (recur file (rest data)))))


(defn new-secure-random-generator []
  (SecureRandom/getInstance "SHA1PRNG"))

(defn rand-int-seq
  "produce a lazy sequence of random 31 bit posative ints"
  ([]
     (rand-int-seq (new-secure-random-generator)))
  ([prng]
     (repeatedly #(. prng nextInt))))

(defn make-pos [numbers]
  "make a sequence of number all positive."
  (map #(if (neg? %) (* -1 %) %) numbers))

(defn rand-seq
  "produce a lazy sequence of arbitraryly large random numbers"
  ([] (rand-seq nil))
  ([limit]
     (let [number-of-output-bits
           (if (nil? limit)
             32
             (count-bits limit))
           random-numbers
           (let [random-31-bit-ints (make-pos (rand-int-seq))]
             (block-seq 31
                        number-of-output-bits
                        random-31-bit-ints))]
       (if (nil? limit)
         random-numbers
         (filter #(< % limit) random-numbers)))))


(defn seq-counter 
  "calls callback after every n'th entry in sequence is evaluated. 
  Optionally takes another callback to call once the seq is fully evaluated."
  ([sequence n callback]
     (map #(do (if (= (rem %1 n) 0) (callback)) %2) (iterate inc 1) sequence))
  ([sequence n callback finished-callback]
     (drop-last (lazy-cat (seq-counter sequence n callback) 
                          (lazy-seq (cons (finished-callback) ()))))))
