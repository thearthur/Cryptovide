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
       :doc "tests for com.clojure.encrypt"}
 com.cryptovide.encryptTest
 (:gen-class)
 (:use
   com.cryptovide.encrypt
   com.cryptovide.misc
   com.cryptovide.testlib
   clojure.contrib.test-is
   [clojure.contrib.duck-streams :only (reader writer)]))

(deftest test-encrypt-file
  (create-test-file)
  (let [file1 (str input-file-name "1")
        file2 (str input-file-name "2")
        file3 (str input-file-name "3")
	file4 (str input-file-name "4")]
    (with-fake-prng
      (encrypt-file input-file-name [file1 file2 file3 file4] 3))
    (is (= (slurp file1) "001 000119 000108 000106 000035 000117 000122 000108 000103 000112 000035 \n000102 000119 000114 000123 000115 000035 000106 000116 000123 000036 \n000111 000120 000113 000117 000104 000104 000037 000114 000122 000106 \n000117 000036 000121 000107 000105 000037 000111 000101 000127 000124 \n000036 000105 000114 000107 \n"))
    (is (= (slurp file2) "002 000126 000114 000117 000042 000123 000133 000115 000109 000123 000042 \n000108 000130 000121 000129 000126 000042 000112 000127 000130 000042 \n000122 000127 000119 000128 000111 000110 000048 000121 000128 000117 \n000124 000042 000132 000114 000111 000048 000118 000107 000138 000131 \n000042 000116 000121 000113 \n"))
    (is (= (slurp file3) "003 000137 000122 000134 000053 000131 000150 000126 000117 000140 000053 \n000116 000147 000132 000137 000143 000053 000120 000144 000141 000050 \n000139 000138 000127 000145 000122 000118 000065 000132 000136 000134 \n000135 000050 000149 000125 000119 000065 000129 000115 000155 000142 \n000050 000133 000132 000121 \n"))
    (is (= (slurp file4) "004 000152 000132 000157 000068 000141 000173 000141 000127 000163 000068 \n000126 000170 000147 000147 000166 000068 000130 000167 000156 000060 \n000162 000153 000137 000168 000137 000128 000088 000147 000146 000157 \n000150 000060 000172 000140 000129 000088 000144 000125 000178 000157 \n000060 000156 000147 000131 \n"))))
