(ns flog
  (:import (org.slf4j Logger LoggerFactory MDC)))


;;;; MDC stuff
(defn put [key val]
  (MDC/put (name key) (str val)))

(defmacro with-context [bindings & body]
  `(let [current-context-map# (MDC/getCopyOfContextMap)
         binding-pairs# (partition 2 ~bindings)]
     (doseq [[k# v#] binding-pairs#]
       (put k# v#))
     (try
       ~@body
       (finally
        (if current-context-map#
          (MDC/setContextMap current-context-map#)
          (MDC/clear))))))
