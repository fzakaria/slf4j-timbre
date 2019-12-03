(ns slf4j-timbre.factory
	(:gen-class
		:name com.github.fzakaria.slf4j.timbre.TimbreLoggerFactory
		:implements [org.slf4j.ILoggerFactory]
		:state state
		:init init)
	(:require slf4j-timbre.adapter
						[taoensso.timbre :as timbre])
	(:import
		(com.github.fzakaria.slf4j.timbre TimbreLoggerFactory TimbreLoggerAdapter)))

(defonce bootstrapped (atom false))

(defn -init
	[]
	(when-not @bootstrapped
		(reset! bootstrapped true)
		(let [lvl (System/getProperty "TIMBRE_LEVEL" ":info")]
			(timbre/set-level! (keyword (subs lvl 1)))))
	[[] (atom {})])

(defn -getLogger
	[^TimbreLoggerFactory this logger-name]
	(let [loggers (.state this)]
		(or
			(get @loggers logger-name)
			(let [new-logger (TimbreLoggerAdapter. logger-name)]
				(-> loggers
					(swap! update logger-name #(or % new-logger))
					(get logger-name)))
			(throw (ex-info (str "Failed to get a logger for " logger-name) {:this this})))))