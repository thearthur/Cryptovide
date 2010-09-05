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
    #^{:author "Arthur Ulfeldt"
       :doc "hashed sequences"}
  com.cryptovide.checksumTest
  (:gen-class)
  (:use com.cryptovide.checksum
        com.cryptovide.testlib
        clojure.contrib.test-is)
  (:import (java.io.UnsupportedEncodingException)
	   (java.security MessageDigest NoSuchAlgorithmException)))

(defn hash-and-stringify [stuff]
  (map str (first (message-digest-seq stuff :sha-1))))

(deftest hash-same-number
  (let [md (message-digest-seq [1 1 1] :sha-1)]
    (are (= (map str (nth md _1)) _2)
         0 ["-65" "-117" "69" "48" "-40" "-46" "70"
            "-35" "116" "-84" "83" "-95" "52" "113"
            "-69" "-95" "121" "65" "-33" "-9"]
         1 ["-111" "89" "-53" "-117" "-50" "-25" "-4"
            "-71" "85" "-126" "-15" "64" "-106" "12"
            "-38" "-25" "39" "-120" "-45" "38"]
         2 ["40" "-40" "108" "86" "-77" "-65" "38"
            "-46" "54" "86" "-101" "-115" "-56" "-61"
            "-7" "31" "50" "-12" "123" "-57"])))

(deftest hash-big-ints
  (are (= (hash-and-stringify [_1]) _2)
       1652342341321243254234 ["34" "-110" "37" "93" "39" "-87" "-6" "-56"
                               "-112" "-44" "122" "-128" "39" "-3" "89" "56"
                               "114" "82" "42" "74"]))

(deftest type-should-not-matter
  (are (= (hash-and-stringify [_1]) ["53" "106" "25" "43" "121" "19" "-80"
                                     "76" "84" "87" "77" "24" "-62" "-115"
                                     "70" "-26" "57" "84" "40" "-85"])
       (byte 49)
       (int 49)
       (bigint 49)))

(deftest hash-nothing
  (is (= (message-digest-seq [] :sha-1) nil)))

(deftest hash-a-byte
  (is (= (hash-and-stringify [(byte 1)])
         ["-65" "-117" "69" "48" "-40" "-46" "70"
          "-35" "116" "-84" "83" "-95" "52" "113"
          "-69" "-95" "121" "65" "-33" "-9"])))

(deftest hash-an-array-of-bytes
  (is (= (hash-and-stringify
          [(into-array Byte/TYPE [(byte 1) (byte 2) (byte 3)])])
         ["112" "55" "-128" "113" "-104" "-62" "42"
          "125" "43" "8" "7" "55" "29" "118"
          "55" "121" "-88" "79" "-33" "-49"])))
