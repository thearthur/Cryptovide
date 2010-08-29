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
     :doc "test library for com.cryptovide.misc"}
 com.cryptovide.miscTest
 (:gen-class)
 (:use
   com.cryptovide.misc
   com.cryptovide.testlib
   clojure.contrib.test-is
   com.cryptovide.modmath
   [clojure.contrib.duck-streams :only (reader writer)]))

(simple-test (count (seque 20000 (range 10))) 10)

(deftest test-seque
  (let [results (map #(= (count (seque 20000 (range %))) %) (range 1000))]
    (is (every? true? results)
      "Clojure core function seque is still broken :(")))

(deftest test-write-seq-to-file
    (with-open [test-file (writer input-file-name)]
      (write-seq-to-file test-file \a (seq "test")))
    (assert-file-contains input-file-name (intify (str \a "test"))))

(deftest test-byte-seq
  (create-test-file)
  (with-open [test-file (reader input-file-name)]
    (let [result (byte-seq test-file)]
      (is (= test-data result)))
    (is (thrown? Exception (. test-file read)))))

(simple-test (block-seq 8 3 [1] (ref 0)) '(1 0 0))
(simple-test (block-seq 8 8 [1 2 3 4] (ref 0)) '(1 2 3 4))
(simple-test (block-seq 8 8 [] (ref 0)) nil)
(simple-test (block-seq 8 8 [0xAA 0xFF] (ref 0)) '(170 255))
(simple-test (block-seq 8 1 [0xAA 0xAA] (ref 0)) 
	     '(0 1 0 1 0 1 0 1 0 1 0 1 0 1 0 1))
(simple-test (block-seq 32 32 [1 2 3 4 5 6 7] (ref 0))
             '(1 2 3 4 5 6 7))
(simple-test (block-seq 32 128 [1 0 0 0 1 0 0 0] (ref 0))
             '(1 1))

(deftest test-bit-shift-right
  (is (= (bit-shift-right 1 32) 0)))

(deftest test-block-padding 
  (let [padding-ref (ref 0)]
    (dorun (block-seq 8 21 [0xAA 0xAA 0xAA] padding-ref))
    (is (= @padding-ref 18))))

(simple-test 
 (block-seq 8 32 (block-seq 32 8 [32769] (ref 0)) (ref 0))
 '(32769))

(simple-test
 (bytes-to-number [0 0 1]) 65536)

(simple-test
 (bytes-to-number [0 0 0 ]) 0)

(simple-test
 (bytes-to-number []) 0)

(simple-test 
 (bytes-to-number (block-seq 32 8 [4] (ref 0))) 4)

(simple-test
 (number-to-bytes 256) [(byte 0) (byte 1)])

(simple-test
 (number-to-bytes 255) [(byte 255)])

(simple-test
 (number-to-bytes 254) [(byte 254)])

(simple-test
 (number-to-bytes 0) [0])

(deftest number-to-bytes-to-number
  (let [lots-of-big-numbers (map #(if (neg? %) (* -1 %) %)
                                 (big-test-numbers))]
    (dorun (map #(is (= (-> %
                            number-to-bytes
                            bytes-to-number)
                        %))
                lots-of-big-numbers))))

(deftest test-butlast-with-callback
  (let [tail (atom ())]
    (is (= (doall (butlast-with-callback (range 20) 15 #(swap! tail (fn [_] %))))))
    (is (= (range 5 20) @tail))))
    
(defn assert-test-seq-counter [len n]
  (let [progress (atom 0)
	seqy (seq-counter (range len) n #(swap! progress inc))]
    (is (= @progress 0))
    (doall seqy)
    @progress))

(deftest test-seq-counter
  (is (= (assert-test-seq-counter 53 3) 17))
  (is (= (assert-test-seq-counter 3 3) 1))
  (is (= (assert-test-seq-counter 1 3) 0))
  (is (= (assert-test-seq-counter 0 0) 0))
  (is (= (assert-test-seq-counter 0 1) 0)))

(deftest test-seq-counter-with-finish-callback
  (let [progress (atom 0)
	finished (atom false)]
    (doall (seq-counter (range 10) 2 #(swap! progress inc) #(reset! finished true)))
    (is (= @progress 5))
    (is (= @finished true))))
  
  
(deftest test-rand-seq
  (let [prng (rand-seq)]
    ; first one to email arthur about the potential false posative
    ; wins the mathmatician's special prize. 
    (is (not= (first prng) (second prng))))
  (let [limited (take 10 (rand-seq 10))]
    (is (reduce #(and %1 %2) (map #(< % 10) limited))))
  (is (= 0 (first (rand-seq 1))))
  (let [big-randoms (rand-seq 40000000000000000000000000000000)]
      (is (= (type (first big-randoms))
             java.math.BigInteger))
      (is (not= (first big-randoms) (second big-randoms)))))
