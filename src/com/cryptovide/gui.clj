(ns #^{:author "Arthur Ulfeldt"
       :doc "the great sticky gui!"}
  com.cryptovide.gui
  (:gen-class)
  (:use com.cryptovide.split)
  (:import (javax.swing JFrame JLabel JTextField JButton)
	   (java.awt.event ActionListener)
	   (java.awt GridLayout)))

(defn gui []
  (let [frame (JFrame. "Cryptovide")
	input-filename (JTextField.)
	input-lable (JLabel. "Input Name")
	go-button (JButton. "GO!")
	new-out-name (fn [] (JLabel. "Out Name"))
	new-out-box (fn [] (JTextField.))
	blabla (JLabel. "blabla")]
    (.addActionListener go-button
       (proxy [ActionListener] []
	 (actionPerformed [event]
			  (let [name (.getText input-filename)]
			    (.setText blabla name)))))
    (doto frame
      (.setLayout (GridLayout. 2 2 3 3))
      (.add input-lable)
      (.add input-filename)
      (.add go-button)
      (.add (new-out-name))
      (.add (new-out-box))
      (.add blabla)
      (.setSize 300 80)
      (.setVisible true))))

;--------------------------------------------------------------------
(defn celsius []
  (let [frame (JFrame. "Celsius Converter")
        temp-text (JTextField.)
        celsius-label (JLabel. "Celsius")
        convert-button (JButton. "Convert")
        fahrenheit-label (JLabel. "Fahrenheit")]
    (.addActionListener convert-button
      (proxy [ActionListener] []
        (actionPerformed [evt]
          (let [c (Double/parseDouble (.getText temp-text))]
            (.setText fahrenheit-label
               (str (+ 32 (* 1.8 c)) " Fahrenheit"))))))
    (doto frame
      (.setLayout (GridLayout. 2 2 3 3))
      (.add temp-text)
      (.add celsius-label)
      (.add convert-button)
      (.add fahrenheit-label)
      (.setSize 300 80)
      (.setVisible true))))
