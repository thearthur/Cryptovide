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
    :doc "common functions for use by tests"}
 com.cryptovide.testlib
 (:gen-class)
 (:use
   com.cryptovide.modmath
   com.cryptovide.misc
   clojure.contrib.test-is))

(def input-file-name "/tmp/testfile")
(def test-size 1000)
(def big 1000000)

(defn test-numbers []
  (take test-size (rand-seq mody)))

(defn big-test-numbers []
  (take test-size (rand-seq)))

(defn in-range? [numbers]
  (every? #(and (>= % 0) (< % mody)) numbers))

(defmacro range-test
  ([fun] `(range-test ~fun (test-numbers) (test-numbers)))
  ([fun & seqs]
     `(let [results# (map ~fun ~@seqs)]
	(is (in-range? results#)
		      (str (quote ~fun) ": result out of range " results#)))))

(defmacro simple-test
  "a shortcut for simple equality test cases"
  [expression output]
  (let [fun# (first expression)]
    `(deftest ~(symbol (str 'fun# (gensym '-test))) []
     (let [output# ~expression
	   expected# ~output]
       (is (= expected# output#) ~(str " from " expression))))))

(defmacro with-fake-prng [ & exprs ]
  "replaces the prng with one that produces consisten results"
  `(binding [com.cryptovide.split/get-prng (fn [] (cycle [1 2 3]))]
     ~@exprs))

(use '[clojure.contrib.duck-streams :only (writer)])
(def test-data (seq (map #(-> % int byte) "the quick brown fox jumped over the lazy dog")))

(defn create-test-filename [suffix]
  "generate a temporary filename"
  (str input-file-name suffix))

(defn create-test-file
  "write some test data to a file with the given suffix"
  ([] (create-test-file ""))
  ([suffix]
    (let [name (create-test-filename suffix)]
      (write-seq-to-file name test-data)
      name)))

(defn assert-file-contains [file expected]
"checks a files contens against a string"
  (is (= expected (intify (slurp file)))))

(comment
(defn try-size [size]
  (binding [com.cryptovide.modmath/mody 
	    (first (filter #(>= (count-bits %) size) primes))
	    com.cryptovide.modmath/field-size size]
     (with-out-str 
       (time (dorun (:data 
		     (first (split 
			     (block-seq 8 size (range 5000000) (ref 0)) 2 3))))))))
(map #(try-size %) (range 8 129))
)
