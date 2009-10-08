(ns 
  #^{:author "Arthur Ulfeldt", 
     :doc "splits input files into secrets and writes output files"}
 com.cryptovide.encrypt
 (:gen-class)
 (:use com.cryptovide.split com.cryptovide.misc
   [clojure.contrib.duck-streams :only (reader writer)]))

(defn write-header [file cyphertext]
  (. file (write (int (:index cyphertext))))
  (. file (write (int (:block-size cyphertext)))))

(defn create-footer [cyphertext]
  (byte (:padding cyphertext)))

(defn split-file [filename parts threshold]
  (let [padding-ref (ref 0)]
    (map 
     #(assoc % :padding padding-ref)
     (split (block-seq 8 default-block-size
		       (byte-seq (reader filename)) padding-ref)
	    threshold parts))))

(defn write-file [cyphertext file-name]
  (with-open [file (writer file-name)]
    (write-header file cyphertext)
    (write-block-seq file (:data cyphertext)
		     (:block-size cyphertext) 
		     (:padding cyphertext))))

(defn encrypt-file [input-file output-file-names threshold]
  (let [parts (count output-file-names)
        cyphertexts (split-file input-file parts threshold)]
    (doall (map write-file cyphertexts output-file-names))))
