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
  (let [test-values (take test-size (get-prng))]
    (is (in-range? test-values )
		  (str "coificient out of range:" test-values))))

(deftest split-test
  (let [result (with-fake-prng (split [0 1 2] 2 3))
        answer (list {:index 1 :block-size field-size :data '(1 3 5) :padding nil}
		     {:index 2 :block-size field-size :data '(2 5 8) :padding nil}
		     {:index 3 :block-size field-size :data '(3 7 11) :padding nil})]
    (is (= answer result) "simple split test")))

(deftest stack-buster-test
  "split a HUGE sequence and see if the stack blows its top"
    (time (is (= (count (:data (first (split (repeat big 42) 2 3)))) big))))
