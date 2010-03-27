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
       :doc "Polynomial threshold encryption"}
  com.cryptovide.main
  (:gen-class)
  (:use
   com.cryptovide.encrypt
   com.cryptovide.decrypt))

(declare main) ; this looks cool, wonder what it does?
(defn usage []
  (println "--encrypt input-file threshold output-file1 output-file2 ...")
  (println "--decrypt output-file part1 part2 part3 ...")
  (System/exit 1))
  
(defn- encrypter [args]
  (if-not (>= (count args) 3)
      (usage))
  (try 
   (let [threshold (Integer/parseInt (second args))
	 input-name (first args)
	 output-names (drop 2 args)]
     (encrypt-file input-name output-names threshold))
   (catch java.lang.NumberFormatException e 
     (println (second args) 
	      " should be the number of parts required to recover the "
	      "origional file")
     (usage))
   (catch java.io.FileNotFoundException e
     (println "could not use file: " e))))

(defn- start-gui []
  (println "assume a GUI"))

(defn- decrypter [args]
  (if (< (count args) 2)
    (usage))
  (decrypt (rest args) (first args)))

(defn -main
  [& args]
  (if (empty? args)
    (start-gui)
    (condp = (first args)
      "--encrypt" (encrypter (rest args))
      "--decrypt" (decrypter (rest args))
      (usage)))
  (System/exit 0))
  
  
  


 
