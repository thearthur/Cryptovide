(ns 
 #^{:author "Arthur Ulfeldt", 
       :doc "miscelanios libs"}
 com.cryptovide.misc
 (:gen-class)
 (:use clojure.contrib.math
       com.cryptovide.modmath
       [clojure.contrib.duck-streams :only (reader writer)]))

(def debug false)
(def default-block-size 8) ;bits

(defstruct secret  :index :block-size :data :padding)


(defn bytes-to-int 
  ([bytes] (bytes-to-int 4 bytes))
  ([num bytes] 
     (let [powers (take num (iterate #(* % 256) 1))]
       (reduce + 0 (map * bytes powers)))))

(defn stringify [ints]
  "maps a seq of ints into a string"
    (apply str (map char ints)))

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

; I dont like passing in a reference to collect the ammount of 
; padding that was/will-be added to the last block of the sequence.
(defn block-seq
  "converts a seq from in-block-size to out-block-size"
  [in-block-size out-block-size bytes padding-ref]
  (letfn [(make-seq 
	   ([in-block-size out-block-size bytes bits length padding-ref]
	     (if (>= length out-block-size)
	       (lazy-seq
		 (cons
		  (extract-bits bits out-block-size 0)
		  (make-seq in-block-size out-block-size bytes
			     (bit-shift-right bits out-block-size)
			     (- length out-block-size) padding-ref)))
	       (dosync
		(let [some-bits  (first bytes)
		      more-bytes (rest bytes)]
		  (if (nil? some-bits)             ;when we cant get more bits
		    (if (= length 0) 
		      nil                          ;end the seq if no leftover bits
		      (do
			(alter padding-ref + (- out-block-size length)) ;save the padding
			(cons bits nil))) ; pad the partial block at the end
		    (make-seq in-block-size out-block-size more-bytes
			       (bit-or bits (bit-shift-left some-bits length))
			       (+ length in-block-size) padding-ref)))))))]
    (make-seq in-block-size out-block-size bytes 0 0 padding-ref)))


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
      (map #(. file (write %))
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
	      #(dosync (commute padding-ref + (bytes-to-int %)))) padding-ref))

(defn write-seq-to-file [file-name & data]
  "writes sequences of things that can be cast to ints, to the open file"
  (with-open [file (writer file-name)]
    (when-not (empty? data)
      (doall (map #(. file (write %))
		  (if (seq? (first data))
		    (map int (first data))
		    (list (int (first data))))))
      (recur file (rest data)))))

(defn rand-seq
  "produce a lazy sequence of random ints < limit"
  ([] (rand-seq nil (new java.util.Random)))
  ([limit]
     (rand-seq limit (new java.util.Random)))
  ([limit prng]
     (lazy-seq
       (cons (if limit
               (. prng nextInt limit)
               (. prng nextInt))
         (rand-seq limit prng)))))

(defn seq-counter 
  "calls callback after every n'th entry in sequence is evaluated. Optionally takes another callback to call once the seq is fully evaluated."
  ([sequence n callback]
     (map #(do (if (= (rem %1 n) 0) (callback)) %2) (iterate inc 1) sequence))
  ([sequence n callback finished-callback]
     (drop-last (lazy-cat (seq-counter sequence n callback) 
			  (lazy-seq (cons (finished-callback) ()))))))
