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
;;     copyright Arthur Ulfeldt 2009,2010 


(ns 
    #^{:author "Arthur Ulfeldt", 
       :doc "Polynomial threshold encryption"}
  com.cryptovide.log
  (:gen-class)
  (:use
   clojure.contrib.logging))


(def logger (org.apache.log4j.Logger/getLogger "A1"))
(def log-levels (vec ( org.apache.log4j.Level/getAllPossiblePriorities)))

(def all-namespaces
     ['com.cryptovide.modmath
      'com.cryptovide.combine
      'com.cryptovide.split
      'com.cryptovide.encrypt
      'com.cryptovide.misc
      'com.cryptovide.decrypt
      'com.cryptovide.gui
      'com.cryptovide.checksum])

;;; See the inspirational SO question: http://stackoverflow.com/questions/3346382

(require 'clojure.contrib.trace)

(defn trace-ns
  "Replaces each function from the given namespace with a version wrapped
  in a tracing call. Can be undone with untrace-ns. ns should be a namespace
  object or a symbol."
  [ns]
  (doseq [s (keys (ns-interns ns))
          :let [v (ns-resolve ns s)]
          :when (and (ifn? @v) (-> v meta :macro not))]
    (intern ns
            (with-meta s {:traced @v})
            (let [f @v] (fn [& args]
                          (clojure.contrib.trace/trace (str "entering: " s))
                          (apply f args))))))

(defn untrace-ns
  "Reverses the effect of trace-ns, replacing each traced function from the
  given namespace with the original, untraced version."
  [ns]
  (doseq [s (keys (ns-interns ns))
          :let [v (ns-resolve ns s)]
          :when (:traced (meta v))]
    (alter-meta! (intern ns s (:traced (meta v)))
                 dissoc :traced)))

(defn start-logging []
  (org.apache.log4j.BasicConfigurator/configure)
  (dorun (map trace-ns all-namespaces)))

(defn stop-logging []
  (dorun (map untrace-ns all-namespaces)))
