(ns com.cryptovide.modmathTest)
(use 'org.gnufoo.unit-test.unit-test)
(use 'com.cryptovide.testlib)
(use 'com.cryptovide.modmath)

(deftest mod*-test []
  (range-test mod*))

(deftest mod*-ugly-number-test []
  (range-test mod* (big-test-numbers) (big-test-numbers)))

(deftest mod+-test []
  (range-test mod+))

(deftest mod+-ugly-number-test []
  (range-test mod+ (big-test-numbers) (big-test-numbers)))

(deftest mod--test []
  (range-test mod-))

(deftest mod--ugly-number-test []
  (range-test mod- (big-test-numbers) (big-test-numbers)))

(defn verrify-modinv [x]
  (if (and (in-range? [x]) (pos? x))
    (let [inv (modinv x)]
      (assert-equal 1 (mod* inv x) (str "x:"x" (inv x):"inv)))
    (assert-expect Exception (modinv x) (str "inverting invalid number " x))))

(deftest modinv-test []
  (verrify-modinv 0)
  (doall (map verrify-modinv (test-numbers))))

(deftest modinv-test-big-numbers []
  (doall (map verrify-modinv (big-test-numbers)))) ; 33 [0]1 43 45 32 24 
  
(defn verrify-powers [x]
  (let [result (take test-size (powers x))]
    (assert-true (in-range? result) (str "powers out of range" x result))))

(deftest powers-test []
  (doall (map verrify-powers (test-numbers)))
  (assert-equal 1 (first (powers 0)) (str "x^0 = 1 got " (first (powers 0))))
  (assert-equal 0 (second (powers 0))(str "x^0 = 1 got " (first (powers 0)))))

(defn verrify-eval-poly [coificients x]
  (assert-true (in-range? (take test-size (powers x)))))

(deftest eval-poly-test []
  (assert-equal 0 (eval-poly [0] 1) "0x^0=0")
  (assert-equal 1 (eval-poly [1] 1) "1x^0=1")
  (verrify-eval-poly (test-numbers) (first (test-numbers))))
	