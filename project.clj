(defproject cryptovide "0.1"
    :dependencies [[org.clojure/clojure "1.2.0"]
                   [org.clojure/clojure-contrib "1.2.0"]
                   [gloss "0.1.1-SNAPSHOT"]
                   [log4j "1.2.15" :exclusions [javax.mail/mail
                                                javax.jms/jms
                                                com.sun.jdmk/jmxtools
                                                com.sun.jmx/jmxri]]]
    :dev-dependencies [[swank-clojure "1.2.1"]
                       [lein-eclipse "1.0.0"]]
    :main com.cryptovide.main)
