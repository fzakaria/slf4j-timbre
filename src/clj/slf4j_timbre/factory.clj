(ns slf4j-timbre.factory
	(:gen-class
		:name com.github.fzakaria.slf4j.timbre.TimbreLoggerFactory
		:implements [org.slf4j.ILoggerFactory]
		:state state
		:init init)
	(:require slf4j-timbre.adapter)
	(:import com.github.fzakaria.slf4j.timbre.TimbreLoggerAdapter))

(defn -init
	[]
	[[] (ref {})])

(defn -getLogger
	[this logger-name]
	(dosync
		(let [loggers (.state this)]
			(if-let [existing (get @loggers logger-name)]
				existing
				(let [new-logger (TimbreLoggerAdapter.)]
					(alter loggers assoc logger-name new-logger)
					new-logger)))))