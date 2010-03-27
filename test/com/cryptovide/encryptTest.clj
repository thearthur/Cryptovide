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
    (assert-file-contains file1 '(1 10 119 176 161 198 8 117 232 193
				  198 25 112 140 96 198 29 114 236 49
				  199 8 106 208 177 7 9 111 224 17
				  71 29 104 160 81 130 28 122 168 81
				  7 9 121 172 145 70 9 111 148 241
				  7 31 36 164 33 199 26 0 0 0 0))
    (assert-file-contains file2 '(2 10 126 200 81 135 10 123 20 50
				  71 27 123 168 192 134 32 121 4 226
				  135 10 112 252 33 136 10 122 252 113
				  7 32 111 184 1 67 30 128 212 193
				  135 10 132 200 241 6 12 118 172 161
				  200 32 42 208 145 71 28 0 0 0 0))
    (assert-file-contains file3 '(3 10 137 232 97 72 13 131 88 226 
				  71 29 140 212 64 199 36 132 36 242
				  72 13 120 64 210 136 12 139 40 242
				  71 36 122 216 17 4 33 136 24 114
				  136 12 149 244 113 71 16 129 204 
				  177 137 35 50 20 66 72 30 0 0 0 0))
    (assert-file-contains file4 '(4 10 152 16 210 9 17 141 180 210 
				  200 31 163 16 225 135 42 147 76 98
				  10 17 130 156 194 9 15 162 100 146
				  8 42 137 0 130 197 36 146 116 98
				  9 15 172 48 18 8 22 144 244 33
				  75 39 60 112 50 201 32 0 0 0 0))))
