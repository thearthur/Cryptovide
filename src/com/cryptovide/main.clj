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
  
  
  


 
