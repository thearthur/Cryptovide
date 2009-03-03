(ns com.cryptovide.decrypt)
(use 'com.cryptovide.misc)
(use 'com.cryptovide.combine)
(use '[clojure.contrib.duck-streams :only (reader writer)])

(defn open-input-file [file-name]
  (try (reader file-name)
    (catch Exception e (println e))))

(defn open-input-files [file-names]
  (map open-input-file file-names))

(defn read-input-file [open-file]
  (let [index (. open-file read)
        data (byte-seq open-file)]
    {:index index :data data}))

(defn decrypt-files [file-names]
  (let [files (open-input-files file-names)
        cyphertexts (map read-input-file files)]
    (combine (map (fn [m] [(:index m) (:data m)]) cyphertexts))))

(defn decrypt-and-save [input-names output-name]
  (with-open [output-file (writer output-name)]
    (write-seq-to-file output-file (decrypt-files input-names))))
