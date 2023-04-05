(ns slf4j-timbre.service-provider
  (:gen-class :name com.github.fzakaria.slf4j.timbre.TimbreServiceProvider
              :implements [org.slf4j.spi.SLF4JServiceProvider]
              :state state
              :init init)
  (:import (com.github.fzakaria.slf4j.timbre TimbreLoggerFactory)
           (org.slf4j.helpers NOPMDCAdapter BasicMarkerFactory)))

(defn -init []
  [[] (atom {})])

(defn -initialize [this]
  (reset! (.state this)
          {:logger-factory (TimbreLoggerFactory.)
           :marker-factory (BasicMarkerFactory.)
           :mdc-adapter (NOPMDCAdapter.)}))

(defn -getLoggerFactory [this]
  (:logger-factory @(.state this)))

(defn -getMarkerFactory [this]
  (:marker-factory @(.state this)))

(defn -getMDCAdapter [this]
  (:mdc-adapter @(.state this)))

(defn -getRequestedApiVersion [_]
  "2.0.99")