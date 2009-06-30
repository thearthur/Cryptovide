(add-classpath "file:///home/arthur/Desktop/hg/src/")
(add-classpath "file:///home/arthur/Desktop/hg/build/")

(use 
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
 'com.cryptovide.cryptovideTest)

(cryptovideTest)