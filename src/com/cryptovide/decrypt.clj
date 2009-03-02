(ns com.cryptovide.decrypt)
(use 'com.cryptovide.misc)
(use 'com.cryptovide.combine)
(use '[clojure.contrib.duck-streams :only (reader writer)])

(defn open-input-file [file-name]
  (try (byte-seq (reader file-name))
    (catch Exception e (println e))))

(defn open-input-files [file-names]
  (map open-input-file file-names))