(ns com.cryptovide.encrypt)
(use 'com.cryptovide.split)
(use 'com.cryptovide.misc)

(use '[clojure.contrib.duck-streams :only (reader writer)])
;(use '[org.clojure.clojure-contrib.duck-streams :only (reader writer)])
(defn split-file [filename parts threshold]
  (split (map int (byte-seq (reader filename))) 3 4))

(defn write-file [index cyphertext file-name]
  (with-open [file (writer file-name)]
    (write-seq-to-file file index cyphertext)))

(defn encrypt-file [input-file output-file-names threshold]
  (let [parts (count output-file-names)
        cyphertext-seq (split-file input-file parts threshold)
        indexes (map first cyphertext-seq)
        cyphertexts (map second cyphertext-seq)]
    (doall (map write-file indexes cyphertexts output-file-names))))
    

    
    