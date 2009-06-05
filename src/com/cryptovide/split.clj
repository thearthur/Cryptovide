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
  (let [splitter
        (fn this [secrets threshold coificients x]
          (when-not (or (empty? secrets)
                      (empty? coificients)
                      (neg? x))
            (let [ [current-coificients rest-coificients]
                  (split-at (dec threshold) coificients) ]
              (lazy-seq
                (cons
                  (eval-poly (cons (first secrets) current-coificients) x)
                  ;TODO: fix this stack consuming recursion
                  (this (rest secrets) threshold rest-coificients x))))))]
    (map (fn [x] (struct secret x 1 (splitter secrets threshold coificients x)))
      (take parts (range 1 (inc parts))))))

