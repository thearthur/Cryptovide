(ns
 #^{:author "Arthur Ulfeldt",
       :doc "handles splitting sequences into secrets. each secret contains some meta data and a sequence of cyphertext"}
 com.cryptovide.split
 (:gen-class)
 (:use
   com.cryptovide.modmath
   com.cryptovide.misc))

(defn get-prng [] (rand-seq mody))

(defn split-chunk [chunk threshold x coificients]
  (map #(eval-poly (cons %1 %2) x)
       chunk (partition (dec threshold) coificients)))

(def chunk-size 3000)
		      
(defn split-chunks [secrets threshold x coificients]
  (flatten
   (map #(split-chunk %1 threshold x %2)
	(partition chunk-size chunk-size () secrets)
	(partition (* chunk-size 
		      (max 1 (dec threshold))) 
		   coificients))))

(defn split 
  ([secrets threshold parts]
     "create lazy seq of secrets applied to a polynomial of threshold degree"
     (let [spliter (fn [x coificients]
		      (split-chunks secrets threshold x coificients))
	   prng (get-prng)]
       (map (fn [x] (struct secret x field-size (spliter x prng)))
	    (take parts (range 1 (inc parts)))))))