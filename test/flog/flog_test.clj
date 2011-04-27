(ns flog.flog-test
  (:use flog :reload)
  (:use clojure.test
        [clojure.contrib.str-utils2 :only (chomp)])
  (:require [clojure.java.io :as io])
  (:import (org.slf4j MDC LoggerFactory Logger)))

(defn clear-context []
  (MDC/clear))

(defn test-clear-context []
  (is (nil? (MDC/getCopyOfContextMap))))

(defn check-val
  ([key]
     (is (MDC/get key)))
  ([key val]
     (is (= val (MDC/get key)))))

(defn check-nil [key]
  (is (nil? (MDC/get key))))

(deftest test-nested-contexts
  (testing "Context is cleared after wrapper exits"
    (clear-context)
    (test-clear-context)
    (with-context ["a" "1"]
      (check-val "a" "1"))
    (test-clear-context)
    (MDC/put "a" "1")
    (with-context ["b" "2"]
      (check-val "a" "1")
      (check-val "b" "2"))
    (check-nil "b")
    (check-val "a" "1"))
  (testing "Contexts are nested properly"
    (clear-context)
    (with-context ["a" "1"]
      (with-context ["b" "2"]
        (check-val "a" "1")
        (check-val "b" "2"))
      (check-val "a" "1")
      (check-nil "b"))
    (check-nil "a"))
  (testing "Contexts are unstacked properly"
    (clear-context)
    (with-context ["a" "1"]
      (with-context ["a" "2"]
        (check-val "a" "2"))
      (check-val "a" "1"))
    (test-clear-context))
  (testing "Contexts are always cleared"
    (clear-context)
    (try
      (with-context ["a" "1"]
        (throw (RuntimeException.)))
      (catch Throwable e))
    (test-clear-context)))

(deftest test-non-string-args
  (testing "Keys can be keywords"
    (clear-context)
    (with-context [:a "a"]
      (check-val "a" "a")))
  (testing "Values can be numbers"
    (with-context ["a" 1]
      (check-val "a" "1")))
  (test-clear-context))

(deftest test-multiple-entries
  (clear-context)
  (with-context [:a 1 :b 2 :c 3 :d 4]
    (check-val "a" "1")
    (check-val "b" "2")
    (check-val "c" "3")
    (check-val "d" "4"))
  (test-clear-context))

(deftest test-no-entries
  (clear-context)
  (with-context []
    (test-clear-context)))

(deftest test-sifter
  (let [log-1 "log/1.log"
        log-2 "log/2.log"
        log-not-set "log/not-set.log"
        msg-1 "This is a log entry for key 1"
        msg-2 "This is a log entry for key 2"
        msg-not-set "This is a log entry without a key"]
    (doseq [log [log-1 log-2 log-not-set]]
      (try
        (io/delete-file log)
        (catch Throwable e)))
    (let [log (LoggerFactory/getLogger "test")]
      (.debug log msg-not-set)
      (with-context [:my-key 1]
        (.debug log msg-1))
      (with-context [:my-key 2]
        (.debug log msg-2))
      (let [file-1 (chomp (slurp log-1))]
        (is (= file-1 msg-1)))
      (let [file-2 (chomp (slurp log-2))]
        (is (= file-2 msg-2)))
      (let [file-not-set (chomp (slurp log-not-set))]
        (is (= file-not-set msg-not-set)))))) 