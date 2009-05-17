(comment
Sample clojure source file
)
(ns com.cryptovide.main
    (:gen-class))
(use 'com.cryptovide.split)
(use 'com.cryptovide.combine)
(use 'com.cryptovide.cryptovideTest)
(defn -main
  []
  (let [result (seq (split [1 2 3 4] 2 3))]
    (println (str "result is " result ))
    (println (str "combined result is " (seq (combine result)))))
  (cryptovideTest)
  (System/exit 0))

 
