(ns flog-test
  (:use flog :reload)
  (:use clojure.test)
  (:import (org.slf4j MDC)))

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