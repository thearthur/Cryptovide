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
;;     copyright Arthur Ulfeldt 2010 

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
