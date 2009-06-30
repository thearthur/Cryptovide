(ns
 #^{:author "Arthur Ulfeldt",
       :doc "handles splitting sequences into secrets. each secret contains some meta data and a sequence of cyphertext"}
 com.cryptovide.split
 (:gen-class)
 (:use
   com.cryptovide.modmath
   com.cryptovide.misc))

(defn get-prng [] (rand-seq mody))

(defn split 
  ([secrets threshold parts]
     "create lazy seq of secrets applied to a polynomial of threshold degree"
     (split secrets threshold parts default-block-size))
  ([secrets threshold parts block-size]
     (let [ spliter (fn [x coificients]
		      (map #(eval-poly (cons %1 %2) x)
			   secrets (partition (dec threshold) coificients)))
	   prng (get-prng)]
       (map (fn [x] (struct secret x block-size (spliter x prng)))
	    (take parts (range 1 (inc parts)))))))

