(ns com.cryptovide.misc)
(def debug false)
(def buffer-size 2)

;this is coppied from clojure/src/clj/clojure/core.clj 
(import '(java.util.concurrent BlockingQueue LinkedBlockingQueue))

(defn non-blocking-seque
  "The same as seque except it uses send instead of send-off for filling the queue
   This will cause it to use the standard agent thread pool which should be closer
   to the optimal thread pool size for the number of CPUs.
   This MUST NOT BE USE FOR secuences that can block"
  ([s] (seque 100 s))
  ([n-or-q s]
   (let [#^BlockingQueue q (if (instance? BlockingQueue n-or-q)
                             n-or-q
                             (LinkedBlockingQueue. (int n-or-q)))
         NIL (Object.) ;nil sentinel since LBQ doesn't support nils
         agt (agent (seq s))
         fill (fn [s]
                (try
                  (loop [[x & xs :as s] s]
                    (if s
                      (if (.offer q (if (nil? x) NIL x))
                        (recur xs)
                        s)
                      (.put q q))) ; q itself is eos sentinel
                  (catch Exception e
                    (.put q q)
                    (throw e)))) 
         drain (fn drain []
                 (let [x (.take q)]
                   (if (identical? x q) ;q itself is eos sentinel
                     @agt  ;will be nil - touch agent just to propagate errors
                     (do
                       (send agt fill)
                       (lazy-cons (if (identical? x NIL) nil x) (drain))))))]
     (send agt fill)
     (drain))))

(defn my-partition 
  "like partition except it wont drop the remainder at the end of the seq"
  [n coll]
  (when (seq coll)
    (let [[chunk rest-coll] (split-at n coll)]
      (lazy-cons chunk (my-partition n rest-coll)))))


(def agent-pool [ (agent ()) (agent ()) (agent ()) ])
(def agents (repeat agent-pool))


(defn stringify [ints]
  "maps a seq of ints into a string"
    (apply str (map char ints)))

(defn intify [stringy]
 (map int (seq stringy)))