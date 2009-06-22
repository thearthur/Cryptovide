(ns 
 #^{:author "Arthur Ulfeldt", 
    :doc "reads encrypted files and writes decrypted files"}
 com.cryptovide.decrypt
 (:gen-class)
 (:use
   com.cryptovide.misc
   com.cryptovide.combine
   [clojure.contrib.duck-streams :only (reader writer)]))

(defn open-input-file [file-name]
  (try (reader file-name)
    (catch Exception e (println e))))

(defn open-input-files [file-names]
  (map open-input-file file-names))

(defn read-input-file [open-file]
  (let [index (. open-file read)
        block-size (. open-file read)
        data (block-seq block-size (byte-seq open-file))]
    (struct secret index block-size data)))

(defn decrypt-files [file-names]
  (let [files (open-input-files file-names)
        cyphertexts (map read-input-file files)]
    (combine cyphertexts)))

(defn decrypt-and-save [input-names output-name]
  (with-open [output-file (writer output-name)]
    (write-seq-to-file output-file (decrypt-files input-names))))
