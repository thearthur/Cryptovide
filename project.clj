(defproject cryptovide "0.1"
    :dependencies [[org.clojure/clojure "1.1.0"]
                   [org.clojure/clojure-contrib "1.1.0"]
                   [log4j "1.2.15" :exclusions [javax.mail/mail
                                                javax.jms/jms
                                                com.sun.jdmk/jmxtools
                                                com.sun.jmx/jmxri]]]
    :dev-dependencies [[leiningen/lein-swank "1.1.0"]]
    :main com.cryptovide.main)
