(ns 
   #^{:author "Arthur Ulfeldt", 
     :doc "test library for com.cryptovide.misc"}
 com.cryptovide.miscTest
 (:gen-class)
 (:use
   com.cryptovide.misc
   com.cryptovide.testlib
   clojure.contrib.test-is
   com.cryptovide.modmath
   [clojure.contrib.duck-streams :only (reader writer)]))

(simple-test (count (seque 20000 (range 10))) 10)

(deftest test-seque
  (let [results (map #(= (count (seque 20000 (range %))) %) (range 1000))]
    (is (every? true? results)
      "Clojure core function seque is still broken :(")))

(deftest test-write-seq-to-file
    (with-open [test-file (writer input-file-name)]
      (write-seq-to-file test-file \a (seq "test")))
    (assert-file-contains input-file-name (intify (str \a "test"))))

(deftest test-byte-seq
  (create-test-file)
  (with-open [test-file (reader input-file-name)]
    (let [result (byte-seq test-file)]
      (is (= test-data result)))
    (is (thrown? Exception (. test-file read)))))

(simple-test (block-seq 8 3 [1]) '(1 0))
(simple-test (block-seq 8 8 [1 2 3 4]) '(1 2 3 4))
(simple-test (block-seq 8 [1 2 3 4])  '(1 2 3 4))
(simple-test (block-seq 8 8 []) nil)
(simple-test (block-seq 1 [0xAA 0xAA]) 
	     '(0 1 0 1 0 1 0 1 0 1 0 1 0 1 0 1))