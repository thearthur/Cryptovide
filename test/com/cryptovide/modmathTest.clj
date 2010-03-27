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
 com.cryptovide.modmathTest
 (:gen-class)
 (:use
   com.cryptovide.testlib
   com.cryptovide.modmath
   clojure.contrib.test-is))
 
(deftest mod*-test 
  (range-test mod*))

(deftest mod*-ugly-number-test
  (range-test mod* (big-test-numbers) (big-test-numbers)))

(deftest mod+-test
  (range-test mod+))

(deftest mod+-ugly-number-test
  (range-test mod+ (big-test-numbers) (big-test-numbers)))

(deftest mod--test
  (range-test mod-))

(deftest mod--ugly-number-test
  (range-test mod- (big-test-numbers) (big-test-numbers)))

(defn verrify-modinv [x]
  (if (and (in-range? [x]) (pos? x))
    (let [inv (modinv x)]
      (is (= 1 (mod* inv x)) (str "x:"x" (inv x):"inv)))
    (is (thrown? Exception (modinv x)) (str "inverting invalid number " x))))

(deftest modinv-test 
  (verrify-modinv 0)
  (doall (map verrify-modinv (test-numbers))))

(deftest modinv-test-big-numbers
  (doall (map verrify-modinv (big-test-numbers)))) ; 33 [0]1 43 45 32 24 
  
(defn verrify-powers [x]
  (let [result (take test-size (powers x))]
    (is (in-range? result) (str "powers out of range" x result))))

(deftest powers-test
  (doall (map verrify-powers (test-numbers)))
  (is (= 1 (first (powers 0))) (str "x^0 = 1 got " (first (powers 0))))
  (is (= 0 (second (powers 0))) (str "x^0 = 1 got " (first (powers 0)))))

(defn verrify-eval-poly [coificients x]
  (is (in-range? (take test-size (powers x)))))

(deftest eval-poly-test
  (is (= 0 (eval-poly [0] 1)) "0x^0=0")
  (is (= 1 (eval-poly [1] 1)) "1x^0=1")
  (verrify-eval-poly (test-numbers) (first (test-numbers))))
	
