(ns 
 #^{:author "Arthur Ulfeldt", 
    :doc "runs all the tests"}
 com.cryptovide.cryptovideTest
 (:gen-class)
 (:use
   com.cryptovide.modmathTest
   com.cryptovide.combineTest
   com.cryptovide.splitTest
   com.cryptovide.encryptTest
   com.cryptovide.miscTest
   com.cryptovide.decryptTest
   clojure.contrib.test-is))

(defn cryptovideTest []
  "run the unit tests"
  (println "creating tests")
  (run-tests
    'com.cryptovide.modmathTest
    'com.cryptovide.combineTest
    'com.cryptovide.splitTest
    'com.cryptovide.miscTest
    'com.cryptovide.encryptTest
    'com.cryptovide.decryptTest
    ))