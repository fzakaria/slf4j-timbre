(ns slf4j-timbre.factory
	(:gen-class
		:name com.github.fzakaria.slf4j.timbre.TimbreLoggerFactory
		:implements [org.slf4j.ILoggerFactory]
		:state state
		:init init)
	(:require
		slf4j-timbre.adapter
		[taoensso.timbre :as timbre])
	(:import
		(com.github.fzakaria.slf4j.timbre TimbreLoggerFactory TimbreLoggerAdapter)))

(defonce bootstrapped? (atom false))

(defn -init
	[]
	(when (and (compare-and-set! bootstrapped? false true) (= timbre/*config* timbre/example-config))
		(let [level (System/getProperty "TIMBRE_LEVEL" ":info")]
			(reset! slf4j-timbre.adapter/override-level (keyword (subs level 1))))
		(add-watch #'timbre/*config* ::on-first-config
			(fn [_ _ _ _]
				(reset! slf4j-timbre.adapter/override-level nil)
				(remove-watch #'timbre/*config* ::on-first-config))))
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