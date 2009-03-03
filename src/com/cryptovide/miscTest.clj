(ns com.cryptovide.miscTest)
(use 'com.cryptovide.misc)
(use 'com.cryptovide.testlib)
(use 'clojure.contrib.test-is)
(use 'com.cryptovide.modmath)
(use '[clojure.contrib.duck-streams :only (reader writer)])

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