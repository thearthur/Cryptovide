(ns com.cryptovide.combine)
(use 'com.cryptovide.modmath)
(use 'com.cryptovide.misc)

(defn row
  "produce a single row for a matrix"
  [parts x answer]
  (conj
   (apply vector (reverse (take parts (powers x))))
   answer))

(defn matrix
  "produce a matrix"
  [parts terms answers]
  (map #(row parts %1 %2)
       terms
       answers))

(defn matrices
  "produce lazy sequence of a matrcies"
  [secrets]
  (let [terms (map :index secrets)
        answers (map :data secrets)
        parts (count secrets)
        matrix-seq
        (fn this [answers]
          (when-not (some empty? answers)
            (let [firsts-answers (map first answers)
                  rests-answers (map next answers)]
              (lazy-seq
                (cons
                  (matrix parts terms firsts-answers)
                (this rests-answers))))))]
  (matrix-seq answers)))

(defn corner [matrix]
  (map (fn [m] (subvec m 1)) (next matrix)))

(defn row-op [top bottom]
  (let [ftop (first top)
	fbot (first bottom)]
    (apply vector
	   (map (fn [t b] 
		  (mod-
		   (mod* t fbot)
		   (mod* b ftop)))
		top bottom))))

(defn do-row-ops [matrix]
  (let [top (first matrix)]
    (conj (map (fn [bottom] (row-op top bottom)) (next matrix)) top)))
  
(defn solve [matrix]
  (if debug (println matrix))
  (if (next matrix)
    (recur (corner (do-row-ops matrix)))
    (mod* (second (first matrix)) (modinv (first (first matrix))))))

(defn combine [answers]
  (when-not (empty? answers)
    (map solve (matrices answers))))
