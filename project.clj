(defproject flog "0.0.2-SNAPSHOT"
  :description "A wrapper for logback for creating traces (i.e. data acquisition)"
  :dependencies [[org.clojure/clojure "1.2.0"]
                 [org.clojure/clojure-contrib "1.2.0"]
                 [org.slf4j/slf4j-api "1.6.1"]]
  :dev-dependencies [[swank-clojure "1.4.0-SNAPSHOT"]
                     [clojure-source "1.2.0"]
                     [ch.qos.logback/logback-core "0.9.28"]
                     [ch.qos.logback/logback-classic "0.9.28"]
                     [org.clojure/tools.logging "0.1.2"]]
  :jvm-opts ["-agentlib:jdwp=transport=dt_socket,server=y,suspend=n"])
