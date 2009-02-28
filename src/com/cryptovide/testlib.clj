(ns com.cryptovide.testlib)
(use 'com.cryptovide.modmath)
(use 'com.cryptovide.misc)
(use 'org.gnufoo.unit-test.unit-test)

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
	(assert-true (in-range? results#)
		      (str (quote ~fun) ": result out of range " results#)))))

(defmacro simple-test
  "a shortcut for simple equality test cases"
  [expression output]
  (let [fun# (first expression)]
    `(deftest ~(symbol (str 'fun# (gensym '-test))) []
     (let [output# ~expression
	   expected# ~output]
       (assert-equal expected# output# ~(str " from " expression))))))

(defmacro with-fake-prng [ & exprs ]
  "replaces the prng with one that produces consisten results"
  `(binding [com.cryptovide.split/coificients (cycle [1 2 3])]
     ~@exprs))

(defn assert-file-contains [file expected]
"checks a files contens against a string"
  (assert-equal expected (intify (slurp file))))