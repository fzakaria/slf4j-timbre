(ns slf4j-timbre.timbre-service-provider
  (:import (com.github.fzakaria.slf4j.timbre TimbreLoggerFactory)
           (org.slf4j.helpers BasicMDCAdapter BasicMarkerFactory))
  (:gen-class :name com.github.fzakaria.slf4j.timbre.TimbreServiceProvider
              :implements [org.slf4j.spi.SLF4JServiceProvider]))


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