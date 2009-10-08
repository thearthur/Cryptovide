(ns 
 #^{:author "Arthur Ulfeldt", 
    :doc "reads encrypted files and writes decrypted files"}
 com.cryptovide.decrypt
 (:gen-class)
 (:use
   com.cryptovide.misc
   com.cryptovide.combine
   [clojure.contrib.duck-streams :only (reader writer)]))

(def file-block-size 8)

(defn open-input-file [file-name]
  (try (reader file-name)
    (catch Exception e (println e))))

(defn open-input-files [file-names]
  (map open-input-file file-names))

(defn read-input-file [open-file]
  (let [index (. open-file read)
        block-size (. open-file read)
	padding (ref 0)
        data (read-block-seq open-file block-size padding)]
    (struct secret index block-size data padding)))

(defn decrypt-files [file-names]
  (let [files (open-input-files file-names)]
    (combine (map read-input-file files))))

(defn decrypt [input-names output-name]
  (with-open [output-file (writer output-name)]
    (dorun (write-seq-to-file output-file (decrypt-files input-names)))))
