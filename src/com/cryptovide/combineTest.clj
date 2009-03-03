(ns com.cryptovide.combineTest)
(use 'com.cryptovide.combine)
(use 'com.cryptovide.split)
(use 'com.cryptovide.testlib)
(use 'clojure.contrib.test-is)
(use 'com.cryptovide.modmath)
(println "testing combine.clj")
(simple-test (row 3 0 1) [0 0 1 1])

(simple-test (row 3 2 42) [4 2 1 42])

(simple-test (matrix 3 [1 2 3] [41 42 43])
	     '([1 1 1 41] [4 2 1 42] [9 3 1 43]))

(def test-results (split [1 2 3] 2 3))

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

(deftest split-and-combine
  "split a bunch of numbers and recombine them."
  (let [numbers (test-numbers)
	splits (split numbers 5 10)
	unsplits (combine splits)]
    (is (= numbers unsplits) "faied to combine")))