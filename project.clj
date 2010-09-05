(defproject flog "0.0.1-SNAPSHOT"
  :description "A wrapper for logback for creating traces (i.e. data acquisition)"
  :dependencies [[org.clojure/clojure "1.2.0"]
                   [org.clojure/clojure-contrib "1.2.0"]
                 [ch.qos.logback/logback-core "0.9.21"]
                 [ch.qos.logback/logback-classic "0.9.21"]]
	:dev-dependencies [[swank-clojure "1.2.1"]
	                    [leiningen/lein-swank "1.1.0"]])