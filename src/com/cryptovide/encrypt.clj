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
       :doc "splits input files into secrets and writes output files"}
  com.cryptovide.encrypt
  (:gen-class)
  (:use com.cryptovide.split com.cryptovide.misc
        [clojure.contrib.duck-streams :only (reader writer)]))

(defn- write-header [file cyphertext]
  (.write file (format "%03d " (:index cyphertext)))
  (.write file (format "%03d " (:modulous cyphertext)))
  (.write file "\n"))

(defn write-block-seq
  "writes a sequence of blocks to a file and appends the trailer"
  [file blocks]
    (dorun 
     (map #(do 
             (dorun (map (fn [n] (.write file (format "%03d " n))) %)) 
             (.write file "\n"))
          (partition-all 10 blocks))))

(defn- split-file [filename parts threshold]
  (split (byte-seq (reader filename))
         threshold parts))

(defn- write-file [cyphertext file-name]
  (with-open [file (writer file-name)]
    (write-header file cyphertext)
    (write-block-seq file (:data cyphertext))))

(defn encrypt-file [input-file output-file-names threshold]
  (let [parts (count output-file-names)
        cyphertexts (split-file input-file parts threshold)]
    (doall (map write-file cyphertexts output-file-names))))
