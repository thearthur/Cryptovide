(ns #^{:author "Arthur Ulfeldt"
       :doc "the great sticky gui!"}
  com.cryptovide.gui
  (:gen-class)
  (:use com.cryptovide.split)
  (:import (javax.swing JFrame JLabel JTextField JButton)
	   (java.awt.event ActionListener)
	   (java.awt GridLayout)))

(def gui-context (ref {:frame nil 
		       :grid [3 1 1 1] 
		       :input-file "name your file"
		       :num-outputs 2
		       :out-names ["file-name" "file name"]
		       :x-size 200
		       :y-size 300
		       :threshold 2}))
(defn setup-gui []
  (dosync 
   (commute gui-context 
	    assoc :frame (JFrame. "Cryptovide"))))

(declare update-gui)
(defn gui-thing [todo] 
  (do
    (todo)
    (update-gui)))

(defn add-output-file []
  (dosync
   (ref-set gui-context 
	    (assoc @gui-context 
	      :x-size (+ (:x-size @gui-context) 100)
	      :out-names (cons "new name" (:out-names @gui-context))))))

(defn update-gui []
  (let [frame (:frame @gui-context)
	input-filename (JTextField.)
	input-lable (JLabel. "Input Name")
	go-button (JButton. "GO!")
	add-file-button (JButton. "bad-ui-thing")
	blabla (JLabel. "blabla")]
    (.addActionListener go-button
       (proxy [ActionListener] []
	 (actionPerformed [event]
			  (gui-thing 
			   #(let [name (.getText input-filename)]
			      (.setText blabla name))))))
    (.addActionListener add-file-button
       (proxy [ActionListener] []
	 (actionPerformed [event]
			  (gui-thing 
			   #(add-output-file)))))
    (doto frame
      (.setLayout (apply #(GridLayout. %1 %2 %3 %4) (:grid @gui-context)))
      (.add input-lable)
      (.add input-filename)
      (.add go-button)
      (.add add-file-button))
      (doall (map (fn [name, i]
		    (letfn [(new-out-name [n] (JLabel. (str "file " n)))
			    (new-out-box  [_] (JTextField.))]
		      (. frame add (new-out-name i))
		      (. frame add (new-out-box name))))
		  (:out-names @gui-context)
		  (range 1)))
      (doto frame
	(.setSize (@gui-context :y-size) (@gui-context :x-size))
	(.setVisible true))))

