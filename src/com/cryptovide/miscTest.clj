(ns com.cryptovide.miscTest)
(use 'com.cryptovide.misc)
(use 'com.cryptovide.testlib)
(use 'org.gnufoo.unit-test.unit-test)
(use 'com.cryptovide.modmath)

(simple-test (count (seque 20000 (range 10))) 10)

(deftest test-seque []
  (let [results (map #(= (count (seque 20000 (range %))) %) (range 1000))]
    (assert-true (every? true? results) "Clojure core function seque is still broken :(")))

(deftest test-non-blocking-seque []
  (let [results (map #(= (count (non-blocking-seque 20000 (range %))) %)
                  (range 1000))]
    (assert-true (every? true? results) "Clojure core function seque is still broken :(")))

