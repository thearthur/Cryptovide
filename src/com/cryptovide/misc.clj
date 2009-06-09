(ns 
 #^{:author "Arthur Ulfeldt", 
       :doc "miscelanios libs"}
 com.cryptovide.misc
 (:gen-class))

(def debug false)
(def buffer-size 2)

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

(defn block-seq [rdr]
  (let [result (. rdr read)]
    (if (= result -1)
      (. rdr close)
      (lazy-seq (cons result (block-seq rdr))))))

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