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
     :doc "Defines the common modulous and common math opperators that use it"}
 com.cryptovide.modmath
 (:use clojure.contrib.lazy-seqs)
 (:gen-class))

(defn count-bits [x]
  (loop [result 1, n x]
    (if (= 0 n)
      (dec result)
      (recur (inc result) (bit-shift-right n 1)))))

(defmacro set-mody [bits]
  "this was too slow"
  `(def mody ~(first (filter #(>= (count-bits %) bits) primes))))
;(set-mody 24)
;(def mody 8388617)

;a 63 bit prime
(def mody 5742664875401159929)

;(def mody 151157)
;;(def mody 719)
(def field-size (count-bits mody))
;;(def mody 11)

(defn sane? [x]
  "range check on x, throws Exception \"number out of range\""
  (if (or (>= x mody) (neg? x))
    (throw (new java.lang.Exception (str "number out of range " x)))))

;;(defn mod [x y] (let [r (rem x y)] (if (neg? r) (+ r y) r)))

(defn mod* [x y] (mod (* x y) mody))
(defn mod+ [x y] (mod (+ x y) mody))
(defn mod- [x y] (let [res (rem (- x y) mody)]
		   (if (neg? res)
		     (+ res mody)
		     res)))

(defn extended_gcd [a, b]
  (do 
    (if (= (rem a b) 0)
      [0, 1]
      (let [[x, y] (extended_gcd b (rem a b))]
        [y, (- x (* y (quot a b)))]))))

(defn modinv [x]
  "x^-1 mod 'mody'"
  (sane? x)
  (if (zero? x)
    (throw (new java.lang.Exception "zero has no inverse")))
  (let [ [gcd inv] (extended_gcd mody x)]
    (if (or (neg? gcd) (neg? inv))
      (mod+ inv mody)
      inv)))

(defn powers
  "produces a lazy seq of the powers of x starting with x^0"
  [x] (iterate (fn [r] (mod* r x)) 1))

(defn eval-poly [coificients x]
  "evaluate a polynomial with the given coificients at x"
  (reduce mod+ (map mod*  coificients (powers x))))
