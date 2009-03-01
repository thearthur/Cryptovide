(ns com.cryptovide.miscTest)
(use 'com.cryptovide.misc)
(use 'com.cryptovide.testlib)
(use 'org.gnufoo.unit-test.unit-test)
(use 'com.cryptovide.modmath)

(simple-test (count (seque 20000 (range 10))) 10)

(deftest test-seque []
  (let [results (map #(= (count (seque 20000 (range %))) %) (range 1000))]
    (assert-true (every? true? results) "Clojure core function seque is still broken :(")))

(deftest test-write-seq-to-file []
    (with-open [test-file (writer input-file-name)]
      (write-seq-to-file test-file \a (seq "test")))
    (assert-file-contains input-file-name (intify (str \a "test"))))

(deftest test-byte-seq []
  (create-test-file)
  (with-open [test-file (reader input-file-name)]
    (let [result (byte-seq test-file)]
      (assert-equal test-data (seq (stringify result))))
    (assert-expect Exception (. test-file read))))