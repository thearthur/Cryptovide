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

(deftest test-multiplex
  (are (= (multiplex _1 _2 _3) _4)
       [1 2 3 4] [:a :b :c :d] 1 [1 :a 2 :b 3 :c 4 :d]
       [1 2 3 4] [:a :b] 2 [1 2 :a 3 4 :b]
       [1 2 3 4] [:a :b :c :d] 1 [1 :a 2 :b 3 :c 4 :d]
       [1 2] [:a :b :c] 1 [1 :a 2 :b]
       [1 2] [:a :b] 3 [1 2 :a]
       [1 2 3 4] [:a :b] 3 [1 2 3 :a 4 :b]))

(deftest test-checksum-sequence
  (let [answer '(0 1 (91 -87 60 -99 -80 -49 -7 63 82 -75
                         33 -41 66 14 67 -10 -19 -94 120 79)
                   2 3 (-96 42 5 -80 37 -71 40 -64 57 -49
                            26 -25 -24 -18 4 -25 -63 -112 -64 -37)
                   4 5 (109 -56 111 17 -72 -51 -66 -121 -101 -8
                            -70 56 50 73 -100 47 -109 -57 41 -70)
                   6 7 (73 65 121 113 74 108 -42 39 35 -99 -2
                           -34 -33 45 -23 -17 -103 76 -81 3)
                   8 9 (-27 31 -105 -103 -60 -94 27 -70 37 92
                            -12 115 -70 -7 90 -119 -31 -72 97 -128)
                   10 11 (86 23 -117 -122 -91 127 -84 34 -119 -102
                             -103 100 24 92 44 -55 110 125 -91 -119)
                   12 13 (-45 91 90 -5 -60 -118 105 104 -105 -64
                              -124 -26 -25 26 -82 103 -57 -51 -108 23))
        answer2 '(1 2 (-65 -117 69 48 -40 -46 70 -35 116 -84
                           83 -95 52 113 -69 -95 121 65 -33 -9)
                    3 4 (18 -38 -38 31 -1 77 71 -121 -83 -29
                            51 49 71 32 44 59 68 62 55 111)
                   5 (:proper-checksum-here))]
    (are (= (print-checksum-seq (checksum-seq _1 _2)) _3)
         (range 20) 2 answer
         [1 2 3 4 5] 2 answer2)))
