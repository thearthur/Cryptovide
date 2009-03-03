(ns com.cryptovide.testlib)
(use 'com.cryptovide.modmath)
(use 'com.cryptovide.misc)
(use 'clojure.contrib.test-is)

(def input-file-name "/tmp/testfile")
(def test-size 1000)
(defn rand-seq
  "produce a lazy sequence of random ints < limit"
  ([] (rand-seq nil (new java.util.Random)))
  ([limit] 
     (rand-seq limit (new java.util.Random)))
  ([limit prng]
     (lazy-cons (if limit
		  (. prng nextInt limit)
		  (. prng nextInt))
		(rand-seq limit prng))))

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
  `(binding [com.cryptovide.split/coificients (cycle [1 2 3])
             com.cryptovide.modmath/mody 719]
     ~@exprs))

(use '[clojure.contrib.duck-streams :only (writer)])
(def test-data (seq (map int "the quick brown fox jumped over the lazy dog")))

(defn create-test-filename [suffix]
  "generate a temporary filename"
  (str input-file-name suffix))

(defn create-test-file
  "write some test data to a file with the given suffix"
  ([] (create-test-file ""))
  ([suffix]
    (let [name (create-test-filename suffix)]
      (with-open [test-file (writer name)]
        (write-seq-to-file test-file test-data))
      name)))

(defn assert-file-contains [file expected]
"checks a files contens against a string"
  (is (= expected (intify (slurp file)))))