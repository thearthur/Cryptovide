(ns com.cryptovide.splitTest)
(use 'com.cryptovide.split)
(use 'com.cryptovide.testlib)
(use 'clojure.contrib.test-is)
(use 'com.cryptovide.modmath)

(deftest coificients-test
  "verrify that the coificients are in range"
  (let [test-values (take test-size coificients)]
    (is (in-range? test-values )
		  (str "coificient out of range:" test-values))))

(deftest split-test
  "rebind coificients to 1 and test split"
  (let [result (with-fake-prng (split [0 1 2] 2 3))
        answer '({:index 1 :data (1 3 5)}
                  {:index 2 :data (2 5 8)}
                  {:index 3 :data (3 7 11)})]
    (is (= answer result) "simple split test")))

