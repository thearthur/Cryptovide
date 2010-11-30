;; This file is part of Cryptovide.
;;
;;     Cryptovide is free software: you can redistribute it and/or modify
;;     it under the terms of the GNU General Public License as published by;;
;;     the Free Software Foundation, either version 3 of the License, or
;;     (at your option) any later version.
;;
;;     Cryptovide is distributed in the hope that it will be useful,
;;     but WITHOUT ANY WARRANTY; without even the implied warranty of
;;     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
;;     GNU General Public License for more details.
;;
;;     You should have received a copy of the GNU General Public License
;;     along with Cryptovide.  If not, see <http://www.gnu.org/licenses/>.
;;
;;     copyright Arthur Ulfeldt 2010

(ns 
  #^{:author "Arthur Ulfeldt", 
    :doc "tests for com.cryptovide.decrypt"}
 com.cryptovide.decryptTest
 (:gen-class)
 (:use
   com.cryptovide.decrypt
   com.cryptovide.misc
   com.cryptovide.modmath
   com.cryptovide.testlib
   clojure.contrib.test-is
   com.cryptovide.encrypt
   [clojure.contrib.duck-streams :only (reader)]))

(deftest test-open-files
  (let [file1 (create-test-file "1")
        file2 (create-test-file "2")
        file3 (create-test-file "3")
        files (open-input-files [file1 file2 file3])]
    (is (= (byte-seq (first files)) test-data))
    (is (= (byte-seq (second files)) test-data))))

(def in-file (create-test-file "decrypt_test"))
(def out-file1 (create-test-filename "_encrypted_1"))
(def out-file2 (create-test-filename "_encrypted_2"))
(with-fake-prng
  (encrypt-file in-file [out-file1 out-file2] 2))

(deftest test-read-header 
  (is (= (read-header (open-input-file "/tmp/testfile_encrypted_1"))
	 {:index 1, :block-size 10})))

(deftest test-read-input-file
  (with-fake-prng 
   (let [ugly-hack (ref 0)]
     (let [result (read-input-file (reader out-file1))]
       (is (=
	    {:index 1
	     :block-size field-size
	     :data '(117 106 104 33 115 120 106 101 110 33 100 117 112 121 113 33 104
			 114 121 34 109 118 111 115 102 102 35 112 120 104 115 34 119
			 105 103 35 109 99 125 122 34 103 112 105)
	     :padding ugly-hack}
	    (assoc result :padding ugly-hack))))
     (let [result (read-input-file (reader out-file2))]
       (is (=
	    {:index 2
	     :block-size field-size
	     :data '(118 108 107 34 117 123 107 103 113 34 102 120 113 123 116 34
			 106 117 122 36 112 119 113 118 103 104 38 113 122 107 116 36
			 122 106 105 38 110 101 128 123 36 106 113 107)
	     :padding ugly-hack}
	    (assoc result :padding ugly-hack)))))))


(deftest test-decrypt-file
  (let [result (decrypt-files [out-file1 out-file2])]
    (is (= test-data result))))

(deftest test-decrypt
  (let [filename (create-test-filename "_decrypted")]
    (decrypt [out-file1 out-file2] filename)
    (assert-file-contains filename test-data)))

(deftest test-decrypt-padding
  (let [input (create-test-filename "input_needs_padding")
        encrypted1 (create-test-filename "encrypted_with_padding1")
        encrypted2 (create-test-filename "encrypted_with_padding2")
        decrypted  (create-test-filename "decrypted_without_padding1")]
    (spit input "a")
    (encrypt-file input [encrypted1 encrypted2] 2)
    (let [files (open-input-files [encrypted1 encrypted2])
          [secret1 secret2] (map read-input-file files)]
      (dorun (:data secret1))
      (dorun (:data secret2))
      (is (= @(:padding secret1) 42))
      (is (= @(:padding secret2) 42)))))
