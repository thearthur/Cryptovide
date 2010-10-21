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
    #^{:author "Arthur Ulfeldt"
       :doc "hashed sequences"}
  com.cryptovide.checksum
  (:gen-class)
  (:use com.cryptovide.misc)
  (:require [clojure.contrib.base64 :as base64])
  (:import (java.io.UnsupportedEncodingException)
	   (java.security MessageDigest NoSuchAlgorithmException)))

(defmulti update-hash #(class %2))

(defmethod update-hash java.lang.Byte [md byte]
  (. md update byte))

(defmethod update-hash (class (into-array Byte/TYPE [])) [md  #^bytes byte-array]
  (. md update byte-array)) 

(defmethod update-hash java.lang.Integer [md inty]
  (. md update (into-array Byte/TYPE (number-to-bytes inty))))

(defmethod update-hash java.math.BigInteger [md inty]
  (. md update (into-array Byte/TYPE (number-to-bytes inty))))

(defn md-seq [seq step md i]
  (if (empty? seq)
    (cons (. md digest) nil)
    (do
      (update-hash md (first seq))  
      (if (empty? (rest seq))
        (cons (. md digest) nil)
        (if (zero? i)
          (lazy-seq
           (let [md-copy (. md clone)]
             (cons (. md digest)
                   (md-seq (rest seq) step md-copy step))))
          (md-seq (rest seq) step md (dec i)))))))

(def md-types
     {:sha-1 "SHA-1"})

(defn message-digest-seq
  ([seq digest-type] (message-digest-seq seq 0 digest-type))
  ([seq step digest-type]
     (md-seq seq step
             (MessageDigest/getInstance (md-types digest-type))
             step)))

(defn multiplex [into insert every-nth]
  "combine two sequences by inserting one into the other at regular intervals"
  (flatten (interleave (partition-all every-nth into)
                       insert)))

(defn de-multiplex [m every-nth]
  "splits a multiplexed sequence into two sequences"
  (let [chunks (partition (inc every-nth))]
    [(map last chunks) (flatten (map butlast chunks))]))

(defn checksum-seq [data interval]
  "inserts a cheksum of data every interval'th position in the sequence"
  (let [checksums (message-digest-seq data interval :sha-1)]
    (multiplex data checksums interval)))

(defn print-checksum-seq [checksum-seq]
  (map #(if (= (type %)
               (type (byte-array 0)))
          (map int %)
          %)
       checksum-seq))
