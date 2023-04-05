(ns slf4j-timbre.t-service-provider
  (:use midje.sweet)
  (:import (com.github.fzakaria.slf4j.timbre TimbreServiceProvider)))

(let
  [provider (doto (TimbreServiceProvider.) .initialize)
   logger-factory-a (.getLoggerFactory provider)
   logger-factory-b (.getLoggerFactory provider)
   marker-factory-a (.getMarkerFactory provider)
   marker-factory-b (.getMarkerFactory provider)
   mdc-adapter-a (.getMDCAdapter provider)
   mdc-adapter-b (.getMDCAdapter provider)]

  (fact "provider returns a logger factory"
        (nil? logger-factory-a) => false)

  (fact "provider returns same logger factory each time"
        (= logger-factory-a logger-factory-b) => true)

  (fact "provider returns a marker factory"
        (nil? marker-factory-a) => false)

  (fact "provider returns same marker factory each time"
        (= marker-factory-a marker-factory-b) => true)

  (fact "provider returns an MDC adapter"
        (nil? mdc-adapter-a) => false)

  (fact "provider returns same MDC adapter each time"
        (= mdc-adapter-a mdc-adapter-b) => true))