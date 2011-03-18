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
 com.cryptovide.mainTest
 (:gen-class)
 (:use
   com.cryptovide.main
   com.cryptovide.testlib
   com.cryptovide.log
   clojure.contrib.test-is))

(deftest test-parse-args
  (are (= (:verbose-level (parse-args _1)) _2)
       [] 0
       ["-v" "-v"] 2
       ["-v" "--" "-v" "1"] 1
       ["--"] 0
       [""] 0
       ["--verbose" "v" "asd"] 0)
  (are (= (:long-options (parse-args _1)) _2)
       [] '()
       ["--one" "--two"] '("--one" "--two")
       ["--one" "--" "--not-an-arg"] '("--one"))
  (stop-logging))

(deftest test-start-gui
  (is (= (start-gui) 42)))

(def input-name (create-test-file "plaintext"))
(def output-names (map create-test-filename ["1" "2" "3"]))
(def result-name (create-test-filename "result"))

(deftest encrypt-decrypt
  (encrypter (concat (list input-name "2") output-names))
  (decrypter (concat (list result-name) output-names))
  (is (= (slurp result-name) (slurp input-name)))
  (is (= (count (slurp result-name)) (count (slurp input-name)))))

(deftest file-not-found
  (is (= (with-out-str (decrypter ["/tmp/should-not-be-written" "/does/not/exist"]))
         "I'm sorry, I could not use the file named:  /does/not/exist (No such file or directory)\n")))
