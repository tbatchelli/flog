(ns flog
  (:import (org.slf4j Logger LoggerFactory)))

(defmacro deflog [log-name namespace] 
  `(def ~log-name (LoggerFactory/getLogger ~namespace)))

(defmacro flog [logger level message]
  (let [hinted-logger (with-meta logger {:tag 'org.slf4j.Logger})]
    (condp = level
        :trace `(.trace ~hinted-logger ~message)
        :debug `(.debug ~hinted-logger ~message))))

(defmacro debug [logger message]
  `(flog ~logger :debug ~message))

(defmacro trace [logger message]
  `(flog ~logger :trace ~message))


