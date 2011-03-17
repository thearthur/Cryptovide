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
    (is (= (slurp file1) "001 719 \n119 108 106 035 117 122 108 103 112 035 \n102 119 114 123 115 035 106 116 123 036 \n111 120 113 117 104 104 037 114 122 106 \n117 036 121 107 105 037 111 101 127 124 \n036 105 114 107 \n"))
    (is (= (slurp file2) "002 719 \n126 114 117 042 123 133 115 109 123 042 \n108 130 121 129 126 042 112 127 130 042 \n122 127 119 128 111 110 048 121 128 117 \n124 042 132 114 111 048 118 107 138 131 \n042 116 121 113 \n"))
    (is (= (slurp file3) "003 719 \n137 122 134 053 131 150 126 117 140 053 \n116 147 132 137 143 053 120 144 141 050 \n139 138 127 145 122 118 065 132 136 134 \n135 050 149 125 119 065 129 115 155 142 \n050 133 132 121 \n"))
    (is (= (slurp file4) "004 719 \n152 132 157 068 141 173 141 127 163 068 \n126 170 147 147 166 068 130 167 156 060 \n162 153 137 168 137 128 088 147 146 157 \n150 060 172 140 129 088 144 125 178 157 \n060 156 147 131 \n"))))
