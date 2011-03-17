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

(defn read-block-seq
  "reads a sequence of blocks from a file (open with duck-streamd.reader"
  [rdr]
  (flatten (map #(map (fn [x] (Integer/parseInt x))
                   (re-seq #"[0-9]+" %)) (line-seq rdr))))

(defn read-input-file [file-name]
  (let [data-and-header (read-block-seq file-name)]
    {:index (first data-and-header)
     :block-size 16
     :modulous (second data-and-header)
     :data (drop 2 data-and-header)
     :padding nil}))

(defn decrypt-files [file-names]
  (let [files (open-input-files file-names)]
    (combine (map read-input-file files))))

(defn decrypt [input-names output-name]
  (dorun (seq-counter 
          (write-seq-to-file 
           output-name 
           (decrypt-files input-names))
          1
          #(println "progress") 
          #(println "done") )))
