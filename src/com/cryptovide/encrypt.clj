(ns 
  #^{:author "Arthur Ulfeldt", 
     :doc "splits input files into secrets and writes output files"}
 com.cryptovide.encrypt
 (:gen-class)
 (:use
   com.cryptovide.split
   com.cryptovide.misc
   [clojure.contrib.duck-streams :only (reader writer)]))

(defn create-header [cyphertext]
  (list (:index cyphertext) (:block-size cyphertext)))

(defn create-footer [cyphertext]
  (list (:padding cyphertext)))

(defn split-file [filename parts threshold]
  (let [padding-ref (ref 0)]
    (split (block-seq default-block-size (byte-seq (reader filename)) padding-ref)
	   threshold parts default-block-size)))

(defn write-file [cyphertext file-name]
  (with-open [file (writer file-name)]
      (write-seq-to-file file (create-header cyphertext) (:data cyphertext))))

(defn encrypt-file [input-file output-file-names threshold]
  (let [parts (count output-file-names)
        cyphertexts (split-file input-file parts threshold)]
    (doall (map write-file cyphertexts output-file-names))))
    

    
    