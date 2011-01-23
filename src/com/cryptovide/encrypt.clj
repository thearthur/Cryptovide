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
       :doc "splits input files into secrets and writes output files"}
  com.cryptovide.encrypt
  (:gen-class)
  (:use com.cryptovide.split com.cryptovide.misc
        [clojure.contrib.duck-streams :only (reader writer)]))

(defn- write-header [file cyphertext]
  (.write file (format "%03d " (:index cyphertext)))
  (.write file (format "%03d " (:block-size cyphertext))))

(defn- create-footer [cyphertext]
  (byte (:padding cyphertext)))

(defn- split-file [filename parts threshold]
  (let [padding-ref (ref 0)]
    (map 
     #(assoc % :padding padding-ref)
     (split (block-seq 8 default-block-size
		       (byte-seq (reader filename)) padding-ref)
	    threshold parts))))

(defn- write-file [cyphertext file-name]
  (with-open [file (writer file-name)]
    (write-header file cyphertext)
    (write-block-seq file (:data cyphertext)
		     (:block-size cyphertext) 
		     (:padding cyphertext))))

(defn encrypt-file [input-file output-file-names threshold]
  (let [parts (count output-file-names)
        cyphertexts (split-file input-file parts threshold)]
    (doall (map write-file cyphertexts output-file-names))))
