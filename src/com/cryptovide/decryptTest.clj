(ns com.cryptovide.decryptTest)
(use 'com.cryptovide.decrypt)
(use 'com.cryptovide.misc)
(use 'com.cryptovide.testlib)
(use 'org.gnufoo.unit-test.unit-test)

(deftest test-open-files []
  (let [file1 (create-test-file "1")
        file2 (create-test-file "2")
        file3 (create-test-file "3")
        files (open-input-files [file1 file2 file3])]
    (assert-equal (first files) test-data)))
      ;(every? true? (map #(= test-data %) files)))))