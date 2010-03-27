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

(def file-block-size 8)

(defn open-input-file [file-name]
  (try (reader file-name)
    (catch Exception e (println e))))

(defn open-input-files [file-names]
  (map open-input-file file-names))

(defn read-header [open-file]
  {:index (. open-file read)
   :block-size (. open-file read)})

(defn read-input-file [open-file]
  (let [header (read-header open-file)
	index (:index header)
        block-size (:block-size header)
	padding (ref 0)
        data (read-block-seq open-file block-size padding)]
    (struct secret index block-size data padding)))

(defn decrypt-files [file-names]
  (let [files (open-input-files file-names)]
    (combine (map read-input-file files))))

(defn decrypt [input-names output-name]
  (with-open [output-file (writer output-name)]
    (dorun (seq-counter 
	    (write-seq-to-file 
	     output-file 
	     (decrypt-files input-names))
	    1
	    #(println "progress") 
	    #(println "done") ))))
