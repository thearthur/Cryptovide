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

(dosync (ref-set pad 0))                     (map #(java.lang.Integer/toBinaryString %) (block-seq 8 21 [0xAA 0xFF 0xFA] pad))