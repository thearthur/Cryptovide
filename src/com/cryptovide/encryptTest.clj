(ns com.cryptovide.encryptTest)
(use 'com.cryptovide.encrypt)
(use 'com.cryptovide.misc)
(use 'com.cryptovide.testlib)
(use 'org.gnufoo.unit-test.unit-test)
(use '[clojure.contrib.duck-streams :only (reader writer)])
(def test-data (seq "the quick brown fox jumped over the lazy dog"))
(def input-file-name "/tmp/testfile")

(defn create-test-file []
  (with-open [test-file (writer input-file-name)]
    (write-seq-to-file test-file test-data)))

(deftest test-encrypt-file []
  (create-test-file)
  (let [file1 (str input-file-name "1")
        file2 (str input-file-name "2")
        file3 (str input-file-name "3")]
    (with-fake-prng
      (encrypt-file input-file-name [file1 file2 file3] 2))
    (assert-file-contains file1 '(1 119 108 106 35 117 122 108 103 112 35
                                   102 119 114 123 115 35 106 116 123 36 111
                                   120 113 117 104 104 37 114 122 106 117 36
                                   121 107 105 37 111 101 127 124 36 105
                                   114 107))
    (assert-file-contains file2 '(2 126 114 117 42 123 133 115 109 123 42 108
                                   130 121 129 126 42 112 127 130 42 122 127
                                   119 128 111 110 48 121 128 117 124 42 132
                                   114 111 48 118 107 138 131 42 116 121 113))
    (assert-file-contains file3 '(3 137 122 134 53 131 150 126 117 140 53 116
                                   147 132 137 143 53 120 144 141 50 139 138
                                   127 145 122 118 65 132 136 134 135 50 149
                                   125 119 65 129 115 155 142 50 133 132 121))))

