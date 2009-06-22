(ns
 #^{:author "Arthur Ulfeldt",
       :doc "handles splitting sequences into secrets. each secret contains some meta data and a sequence of cyphertext"}
 com.cryptovide.split
 (:gen-class)
 (:use
   com.cryptovide.modmath
   com.cryptovide.misc))

(def coificients (cycle [1 2 3 4 5 6 7 8 ]))
(defn get-prng [] (rand-seq mody))
;(defn get-prng [] coificients)
(def block-size 8) ;bits
(defn split [secrets threshold parts]
  "lazy seqs of the [x (secrets applied to a polynomial of threshold degree )]"
  (let [ spliter (fn [x coificients]
                   (map #(eval-poly (cons %1 %2) x)
                     secrets (partition (dec threshold) coificients)))
        prng (get-prng)]
    (map (fn [x] (struct secret x block-size (spliter x prng)))
      (take parts (range 1 (inc parts))))))

