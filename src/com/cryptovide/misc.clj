(ns 
 #^{:author "Arthur Ulfeldt", 
       :doc "miscelanios libs"}
 com.cryptovide.misc
 (:gen-class)
 (:use clojure.contrib.math))

(def debug false)
(def buffer-size 8)

(defstruct secret  :index :block-size :data)

(defn my-partition 
  "like partition except it wont drop the remainder at the end of the seq"
  [n coll]
  (when (seq coll)
    (let [[chunk rest-coll] (split-at n coll)]
      (lazy-seq (cons chunk (my-partition n rest-coll))))))

(defn stringify [ints]
  "maps a seq of ints into a string"
    (apply str (map char ints)))

(defn intify [stringy]
 (map int (seq stringy)))

(defn byte-seq [rdr]
  (let [result (. rdr read)]
    (if (= result -1)
      (do (. rdr close) nil)
      (lazy-seq (cons result (byte-seq rdr))))))

(defn ones
  ([n] (ones n 1 1))
  ([n i res]
    (if (< i n)
      (recur n (inc i) (bit-set res i))
      res)))

(defn extract-bits
  [bytes len offset]
  (bit-shift-right (bit-and bytes (bit-shift-left (ones len) offset)) offset))

; I dont like passing in a reference to collect the ammount of 
; padding that was/will-be added to the last block of the sequence.
(defn block-seq
  ([block-size bytes padding-ref]
    "reads a byte-seq into a sequence of block-size bits."
    (block-seq 8 block-size bytes padding-ref))
  ([in-block-size out-block-size bytes padding-ref]
    "converts a seq from in-block-size to out-block-size"
    (block-seq in-block-size out-block-size bytes 0 0 padding-ref))
  ([in-block-size out-block-size bytes bits length padding-ref]
    (if (>= length out-block-size)
      (lazy-seq
        (cons
          (extract-bits bits  out-block-size 0)
          (block-seq in-block-size out-block-size bytes
            (bit-shift-right bits out-block-size)
            (- length out-block-size) padding-ref)))
      (dosync
       (let [some-bits  (first bytes)
	     more-bytes (rest bytes)]
	 (assert (< length out-block-size))
	 (if (nil? some-bits)             ;when we cant get more bits
	   (if (= bits 0) 
	     nil                          ;end the seq if no leftover bits
	     (do
	       (alter padding-ref + (- out-block-size length)) 
	       (lazy-seq (cons bits nil)))) ; pad the partial block at the end
	   (block-seq in-block-size out-block-size more-bytes
		      (bit-or bits (bit-shift-left some-bits length))
		      (+ length in-block-size) padding-ref)))))))

(use '[clojure.contrib.duck-streams :only (reader writer)])
(defn write-seq-to-file [file & data]
  "writes sequences of things that can be cast to ints, to the open file"
  (when-not (empty? data)
    (doall (map #(. file (write %))
             (if (seq? (first data))
               (map int (first data))
               (list (int (first data)))
               )))
    (recur file (rest data))))

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
