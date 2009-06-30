(ns 
  #^{:author "Arthur Ulfeldt", 
     :doc "splits input files into secrets and writes output files"}
 com.cryptovide.encrypt
 (:gen-class)
 (:use
   com.cryptovide.split
   com.cryptovide.misc))

(defn create-header [cyphertext]
  (list (:index cyphertext) (:block-size cyphertext)))

(defn create-footer [cyphertext]
  (list (:padding cyphertext)))

(use '[clojure.contrib.duck-streams :only (reader writer)])
(defn split-file [filename parts threshold]
  (let [padding-ref (ref 0)]
    (split (block-seq 8 (byte-seq (reader filename)) padding-ref)
	   parts threshold)))

(defn write-file [cyphertext file-name]
  (with-open [file (writer file-name)]
      (write-seq-to-file file (create-header cyphertext) (:data cyphertext))))

(defn encrypt-file [input-file output-file-names threshold]
  (let [parts (count output-file-names)
        cyphertexts (split-file input-file parts threshold)]
    (doall (map write-file cyphertexts output-file-names))))
    

    
    