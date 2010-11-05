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
   com.cryptovide.gui
   com.cryptovide.checksum
   clojure.contrib.logging
   com.cryptovide.encrypt
   com.cryptovide.decrypt
   com.cryptovide.log))

;(start-logging)

(declare main) ; this looks cool, wonder what it does?
(defn usage []
  (println "--encrypt input-file threshold output-file1 output-file2 ...")
  (println "--decrypt output-file part1 part2 part3 ...")
  (System/exit 1))
  
(defn- encrypter [args]
  (fatal "hello logger")
  (if-not (>= (count args) 3)
    (usage))
  (let [input-name (first args)
        output-names (drop 2 args)]
    (debug (str  "input-name: " input-name "output-names: " output-names))
    (try 
     (let [threshold (Integer/parseInt (second args))]
       (encrypt-file input-name output-names threshold))
     (catch java.lang.NumberFormatException e 
       (println (second args) 
                " should be the number of parts required to recover the "
                "origional file")
       (usage))
     (catch java.io.FileNotFoundException e
       (debug e)
       (println "I'm sorry, I could not use the file named: " input-name))
     (catch Exception badness
       (debug badness)
       (println "I'm sorry, something i hadent expected went wrong, bummer :("
                badness)))))

(defn- start-gui []
  (println "assume a GUI"))


(defn- decrypter [args]
  "combine a set of secrets"
  (if (< (count args) 2)
    (usage))
  (let [output-name (first args)
        input-names (rest args)]
    (try 
     (decrypt input-names output-name)
     (catch java.io.FileNotFoundException e
       (println "I'm sorry, I could not use the file named: " (. e getMessage))))))


(defn parse-args [args]
  "handles basic option parsing. no options with args"
  (let [potential-options (take-while #(not= "--" %) args)
        after-dashes (rest (drop-while #(not= "--" %) args))
        short-options (filter #(and (= (first %) \-) (not= (second %) \-))
                              potential-options)
        long-options (filter #(and (= (first %) \-) (= (second %) \-))
                              potential-options)
        arguments (filter #(not= (first %) \-) potential-options)
        verbose-level (min 4 (count (filter #(= "-v" %)
                                            short-options)))]
    (. logger setLevel (log-levels verbose-level))
    {:verbose-level verbose-level
     :short-options short-options
     :long-options  long-options
     :arguments     (concat arguments after-dashes)}))


(defn -main [& args]    
  "where it all begins :)"
  (if (empty? args)
    (start-gui)
    (let [argument-map (parse-args args)
          decrypt? (some #(= "--decrypt" %) (:long-optioins argument-map))
          encrypt? (some #(= "--encrypt" %) (:long-optioins argument-map))]
      (println argument-map)
      (cond
        (and encrypt? decrypt?) (usage)
        (encrypt? (encrypter (:arguments argument-map)))
        (decrypt? (decrypter (:arguments argument-map)))
        true (usage))))
  (System/exit 0))


