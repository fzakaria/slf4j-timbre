(ns slf4j-timbre.t-factory
  (:use midje.sweet)
  (:import org.slf4j.LoggerFactory
           (com.github.fzakaria.slf4j.timbre TimbreServiceProvider)))

(defn- logger [name]
  (.getLogger (.getLoggerFactory (TimbreServiceProvider.))
              name))

(let
 [logger-1a (logger "logger-1")
  logger-2  (logger "logger-2")
  logger-1b (logger "logger-1")
  logger-1c (future (logger "logger-1"))]

  (fact "factory returns same instances for same names"
        (= logger-1a logger-1b) => true)

  (fact "factory returns different instances for different names"
        (= logger-1a logger-2) => false)

  (fact "factory returns same instances for same names across different threads"
        (= logger-1a @logger-1c) => true))