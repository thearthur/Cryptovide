(ns 
 #^{:author "Arthur Ulfeldt", 
    :doc "test lib for com.cryptovide.modmath"}
 com.cryptovide.splitTest
 (:use
   com.cryptovide.split
   com.cryptovide.testlib
   clojure.contrib.test-is
   com.cryptovide.modmath))

(deftest coificients-test
  "verrify that the coificients are in range"
  (let [test-values (take test-size coificients)]
    (is (in-range? test-values )
		  (str "coificient out of range:" test-values))))

(deftest split-test
  "rebind coificients to 1 and test split"
  (let [result (with-fake-prng (split [0 1 2] 2 3))
        answer '({:index 1 :block-size 1 :data (1 3 5)}
                  {:index 2 :block-size 1 :data (2 5 8)}
                  {:index 3 :block-size 1 :data (3 7 11)})]
    (is (= answer result) "simple split test")))

(deftest stack-buster-test
  "split a HUGE sequence and see if the stack blows its top"
  (split (range 1000000000) 2 3)
  (is true))