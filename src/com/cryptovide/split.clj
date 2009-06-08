(ns
 #^{:author "Arthur Ulfeldt",
       :doc "handles splitting sequences into secrets. each secret contains some meta data and a sequence of cyphertext"}
 com.cryptovide.split
 (:gen-class)
 (:use
   com.cryptovide.modmath
   com.cryptovide.misc))

(def coificients (cycle [1 2 3 4 5 6 7 8 ]))
(defn get-prng [] coificients)

(defn split [secrets threshold parts]
  "lazy seqs of the [x (secrets applied to a polynomial of threshold degree )]"
  (let [ spliter (fn [x coificients]
                   (map #(eval-poly (cons %1 %2) x)
                     secrets (partition (dec threshold) (get-prng))))]
    (map (fn [x] (struct secret x 1 (spliter x coificients )))
      (take parts (range 1 (inc parts))))))

