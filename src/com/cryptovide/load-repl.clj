(defmacro load-all []
  '(use 
   :reload-all
   'com.cryptovide.modmath
   'com.cryptovide.combine
   'com.cryptovide.split
   'com.cryptovide.encrypt
   'com.cryptovide.misc
   'com.cryptovide.decrypt
   'com.cryptovide.modmathTest
   'com.cryptovide.combineTest
   'com.cryptovide.splitTest
   'com.cryptovide.encryptTest
   'com.cryptovide.miscTest
   'com.cryptovide.decryptTest
   'com.cryptovide.testlib
   'com.cryptovide.gui))

(load-all)