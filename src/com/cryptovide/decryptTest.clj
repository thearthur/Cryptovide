(ns com.cryptovide.decryptTest)
(use 'com.cryptovide.decrypt)
(use 'com.cryptovide.misc)
(use 'com.cryptovide.testlib)
(use 'org.gnufoo.unit-test.unit-test)
(use 'com.cryptovide.encrypt)
(use '[clojure.contrib.duck-streams :only (reader)])

(deftest test-open-files []
  (let [file1 (create-test-file "1")
        file2 (create-test-file "2")
        file3 (create-test-file "3")
        files (open-input-files [file1 file2 file3])]
    (assert-equal (byte-seq (first files)) test-data)
    (assert-equal (byte-seq (second files)) test-data)))

(def in-file (create-test-file "decrypt_test"))
(def out-file1 (create-test-filename "_encrypted_1"))
(def out-file2 (create-test-filename "_encrypted_2"))
(with-fake-prng
  (encrypt-file in-file [out-file1 out-file2] 2))

(deftest test-read-input-file []
  (let [result (read-input-file (reader out-file1))]
    (assert-equal
      {:index 1
       :data '(117 106 104 33 115 120 106 101 110 33 100 117 112 121 113 33 104
                114 121 34 109 118 111 115 102 102 35 112 120 104 115 34 119
                105 103 35 109 99 125 122 34 103 112 105)}
      result))
  (let [result (read-input-file (reader out-file2))]
    (assert-equal
      {:index 2
       :data '(118 108 107 34 117 123 107 103 113 34 102 120 113 123 116 34
                106 117 122 36 112 119 113 118 103 104 38 113 122 107 116 36
                122 106 105 38 110 101 128 123 36 106 113 107)}
      result)))


(deftest test-decrypt-file []
  (let [result (decrypt-files [out-file1 out-file2])]
    (assert-equal test-data result)))

(deftest test-decrypt-and-save []
  (let [filename (create-test-filename "_decrypted")]
    (decrypt-and-save [out-file1 out-file2] filename)
    (assert-file-contains filename test-data)))
