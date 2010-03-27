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
    :doc "tests for com.cryptovide.combine"}
 com.cryptovide.combineTest
 (:gen-class)
 (:use
   com.cryptovide.combine
   com.cryptovide.split
   com.cryptovide.testlib
   clojure.contrib.test-is
   com.cryptovide.modmath))

(simple-test (row 3 0 1) [0 0 1 1])

(simple-test (row 3 2 42) [4 2 1 42])

(simple-test (matrix 3 [1 2 3] [41 42 43])
	     '([1 1 1 41] [4 2 1 42] [9 3 1 43]))

(def test-results (with-fake-prng (split [1 2 3] 2 3)))

(simple-test (matrices test-results)
	     '(([1 1 1 2] [4 2 1 3] [9 3 1 4])
	      ([1 1 1 4] [4 2 1 6] [9 3 1 8])
	      ([1 1 1 6] [4 2 1 9] [9 3 1 12])))

(simple-test (corner '([1 1 1 2] [4 2 1 3] [9 3 1 4]))
	     '([2 1 3] [3 1 4]))

(simple-test (row-op [1 2 3] [1 2 3]) [0 0 0])
(simple-test (row-op [1 3 4] [1 2 3]) [0 1 1])
(simple-test (row-op [2 5 7] [1 2 3]) [0 1 1])

(simple-test (do-row-ops '([2 5 7] [1 2 3] [1 2 3])) '([2 5 7] [0 1 1] [0 1 1]))

(simple-test (solve '([1 1 1 2] [4 2 1 3] [9 3 1 4])) 1)

(simple-test (combine (split [0] 1 1)) [0])
(simple-test (combine (split [0] 2 2)) [0])
(simple-test (combine (split [0] 2 20)) [0])
(deftest split-and-combine
  "split a bunch of numbers and recombine them."
  (let [numbers (test-numbers)
	splits (split numbers 5 10)
	unsplits (combine splits)]
    (is (= numbers unsplits) "faied to combine")))

;(deftest time-test
;  (time (dorun (combine (split (range big) 4 4)))))

