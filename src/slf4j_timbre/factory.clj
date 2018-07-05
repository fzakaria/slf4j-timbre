(ns slf4j-timbre.factory
	(:gen-class
		:name com.github.fzakaria.slf4j.timbre.TimbreLoggerFactory
		:implements [org.slf4j.ILoggerFactory]
		:state state
		:init init)
	(:require slf4j-timbre.adapter)
	(:import
		com.github.fzakaria.slf4j.timbre.TimbreLoggerFactory
		com.github.fzakaria.slf4j.timbre.TimbreLoggerAdapter))

(defn -init
	[]
	[[] (atom {})])

(defn -getLogger
	[^TimbreLoggerFactory this logger-name]
	(let [loggers (.state this) loggers-map @loggers]
		(if-let [existing (get loggers-map logger-name)]
			existing
			(let [new-logger (TimbreLoggerAdapter. logger-name)]
				(if (compare-and-set! loggers loggers-map (assoc loggers-map logger-name new-logger))
					new-logger
					(get @loggers logger-name))))))