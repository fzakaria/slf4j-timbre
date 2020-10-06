(ns slf4j-timbre.t-factory
  (:use midje.sweet)
  (:import org.slf4j.LoggerFactory))

(let
 [logger-1a (LoggerFactory/getLogger "logger-1")
  logger-2  (LoggerFactory/getLogger "logger-2")
  logger-1b (LoggerFactory/getLogger "logger-1")
  logger-1c (future (LoggerFactory/getLogger "logger-1"))]

  (fact "factory returns same instances for same names"
        (= logger-1a logger-1b) => true)

  (fact "factory returns different instances for different names"
        (= logger-1a logger-2) => false)

  (fact "factory returns same instances for same names across different threads"
        (= logger-1a @logger-1c) => true))