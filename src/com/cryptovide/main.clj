(comment
Sample clojure source file
)
(ns 
  #^{:author "Arthur Ulfeldt", 
     :doc "Polymorphic threshold encryption"}
  com.cryptovide.main
  (:gen-class)
  (:use
    com.cryptovide.split
    com.cryptovide.combine
    com.cryptovide.cryptovideTest))

(declare main) ; this looks cool, wonder what it does?

(defn -main
  []
  (let [result (seq (split [1 2 3 4] 2 3))]
    (println (str "result is " result ))
    (println (str "combined result is " (seq (combine result)))))
  (cryptovideTest)
  (System/exit 0))

 
