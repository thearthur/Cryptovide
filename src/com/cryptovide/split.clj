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
  (apply concat
   (map #(split-chunk %1 threshold x %2)
	(partition chunk-size chunk-size () secrets)
	(partition (* chunk-size (dec threshold)) coificients))))

;not sure is this function is necessasary
(defn lazy-apply-cat [[cur & more :as seqs]]
  "returns a lazy seq that's like (apply concat list-o-seqs)"
  (if (empty? seqs) 
    nil
    (if (empty? cur)
      (lazy-apply-cat (rest seqs))
      (lazy-seq 
	(cons
	 (first cur)
	 (lazy-apply-cat (cons (rest cur) more)))))))

(defn split 
  ([secrets threshold parts]
     "create lazy seq of secrets applied to a polynomial of threshold degree"
     (let [spliter (fn [x coificients]
		      (split-chunks secrets threshold x coificients))
	   prng (get-prng)]
       (map (fn [x] (struct secret x field-size (spliter x prng)))
	    (take parts (range 1 (inc parts)))))))