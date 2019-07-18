(ns slf4j-timbre.factory
	(:gen-class
		:name com.github.fzakaria.slf4j.timbre.TimbreLoggerFactory
		:implements [org.slf4j.ILoggerFactory]
		:state state
		:init init)
	(:require slf4j-timbre.adapter)
	(:import
		(com.github.fzakaria.slf4j.timbre TimbreLoggerFactory TimbreLoggerAdapter)))

(defn -init
	[]
	[[] (atom {})])

(defn -getLogger
	[^TimbreLoggerFactory this logger-name]
	(or (let [loggers (.state this) loggers-map @loggers]
        (if-let [existing (get loggers-map logger-name)]
          existing
          (let [new-logger (TimbreLoggerAdapter. logger-name)]
            (get (swap! loggers update logger-name #(or % new-logger))
                 logger-name))))
      (throw (ex-info (str "Failed to get a logger for " logger-name) {:this this}))))
