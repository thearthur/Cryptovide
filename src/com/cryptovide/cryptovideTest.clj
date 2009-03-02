(ns com.cryptovide.cryptovideTest)
(use 'org.gnufoo.unit-test.unit-test)
(use 'com.cryptovide.modmathTest)
(use 'com.cryptovide.combineTest)
(use 'com.cryptovide.splitTest)
(use 'com.cryptovide.encryptTest)
(use 'com.cryptovide.miscTest)
(use 'com.cryptovide.decryptTest)

(defn cryptovideTest []
  (println "creating tests")
  (my-run-all-tests
    'com.cryptovide.modmathTest
    'com.cryptovide.combineTest
    'com.cryptovide.splitTest
    'com.cryptovide.miscTest
    'com.cryptovide.encryptTest
    'com.cryptovide.decryptTest
    ))