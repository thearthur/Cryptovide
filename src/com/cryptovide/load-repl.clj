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
   'com.cryptovide.gui
   'com.cryptovide.checksum
   'com.cryptovide.log))

(load-all)
