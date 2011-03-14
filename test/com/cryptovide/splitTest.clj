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
    :doc "test lib for com.cryptovide.modmath"}
 com.cryptovide.splitTest
 (:use
   com.cryptovide.split
   com.cryptovide.testlib
   clojure.contrib.test-is
   com.cryptovide.modmath))

(deftest coificients-test
  "verrify that the coificients are in range"
  (let [test-values (take test-size (get-prng))]
    (is (in-range? test-values)
        (str "coificient out of range:" test-values))))

(deftest split-test
  (let [result (with-fake-prng (split [0 1 2] 2 3))
        answer (list {:index 1 :modulous mody :data '(1 3 5) :padding nil}
		     {:index 2 :modulous mody :data '(2 5 8) :padding nil}
		     {:index 3 :modulous mody :data '(3 7 11) :padding nil})]
    (is (= answer result) "simple split test")))

(deftest stack-buster-test
  "split a HUGE sequence and see if the stack blows its top"
    (time (is (= (count (:data (first (split (repeat big 42) 2 3)))) big))))
