(ns com.cryptovide.splitTest)
(use 'com.cryptovide.split)
(use 'com.cryptovide.testlib)
(use 'org.gnufoo.unit-test.unit-test)
(use 'com.cryptovide.modmath)

(deftest coificients-test []
  "verrify that the coificients are in range"
  (let [test-values (take test-size coificients)]
    (assert-true (in-range? test-values )
		  (str "coificient out of range:" test-values))))

(deftest split-test []
  "rebind coificients to 1 and test split"
  (let [result (with-fake-prng (split [0 1 2] 2 3))
        answer '([1 (1 3 5)] [2 (2 5 8)] [3 (3 7 11)])]
    (assert-equal answer result "simple split test")))

