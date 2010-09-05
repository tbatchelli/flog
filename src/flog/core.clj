(ns flog
  (:import (org.slf4j Logger LoggerFactory)))

(defmacro deflog [log-name namespace] 
                `(def #^org.slf4j.Logger ~log-name (LoggerFactory/getLogger ~namespace)))
(defmacro flog [#^org.slf4j.Logger logger level message]
                (condp = level
                  :trace `(.trace #^org.slf4j.Logger ~logger ~message)
                  :debug `(.debug #^org.slf4j.Logger ~logger ~message)))

(defmacro debug [#^org.slf4j.Logger logger message] `(flog ~logger :debug ~message))