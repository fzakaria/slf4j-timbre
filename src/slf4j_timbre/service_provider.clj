(ns slf4j-timbre.service-provider
  (:gen-class :name com.github.fzakaria.slf4j.timbre.TimbreServiceProvider
              :implements [org.slf4j.spi.SLF4JServiceProvider]
              :state state
              :init init)
  (:import (com.github.fzakaria.slf4j.timbre TimbreLoggerFactory TimbreServiceProvider)
           (org.slf4j.helpers NOPMDCAdapter BasicMarkerFactory)))

(defn -init []
  [[] (atom {})])

(defn -initialize
  [^TimbreServiceProvider this]
  (reset! (.state this)
          {:logger-factory (TimbreLoggerFactory.)
           :marker-factory (BasicMarkerFactory.)
           :mdc-adapter (NOPMDCAdapter.)}))

(defn -getLoggerFactory
  [^TimbreServiceProvider this]
  (:logger-factory @(.state this)))

(defn -getMarkerFactory
  [^TimbreServiceProvider this]
  (:marker-factory @(.state this)))

(defn -getMDCAdapter
  [^TimbreServiceProvider this]
  (:mdc-adapter @(.state this)))

(defn -getRequestedApiVersion [_]
  "2.0.99")