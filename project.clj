(defproject cryptovide "0.1"
    :dependencies [[org.clojure/clojure "1.2.0"]
                   [org.clojure/clojure-contrib "1.2.0"]
                   [log4j "1.2.15" :exclusions [javax.mail/mail
                                                javax.jms/jms
                                                com.sun.jdmk/jmxtools
                                                com.sun.jmx/jmxri]]]
;    :dev-dependencies [[leiningen/lein-swank "1.1.0"]]
    :dev-dependencies [[swank-clojure "1.2.1"]]
    :main com.cryptovide.main)
