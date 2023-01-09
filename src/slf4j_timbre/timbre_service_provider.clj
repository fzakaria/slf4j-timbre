(ns slf4j-timbre.timbre-service-provider
  (:import (com.github.fzakaria.slf4j.timbre TimbreLoggerFactory)
           (org.slf4j ILoggerFactory IMarkerFactory)
           (org.slf4j.helpers BasicMDCAdapter BasicMarkerFactory)
           (org.slf4j.spi MDCAdapter))
  (:gen-class :name com.github.fzakaria.slf4j.timbre.TimbreServiceProvider
              :implements [org.slf4j.spi.SLF4JServiceProvider]
              #_#_:methods [[getLoggerFactory [] org.slf4j.ILoggerFactory]
                        [getMarkerFactory [] org.slf4j.IMarkerFactory]
                        [getMDCAdapter [] org.slf4j.spi.MDCAdapter]
                        [getRequestedApiVersion [] String]
                        [initialize [] void]]
              )
  )


(def ^:private logger-factory
  (TimbreLoggerFactory.))

(def ^:private marker-factory
  (BasicMarkerFactory.))

(def ^:private mdc-adapter
  (BasicMDCAdapter.))


(defn -initialize [_]
  [[]])

(defn -getLoggerFactory [_]
  logger-factory)

(defn -getMarkerFactory [_]
  marker-factory)

(defn -getMDCAdapter [_]
  mdc-adapter)

(defn -getRequestedApiVersion [_]
  "2.0.99")