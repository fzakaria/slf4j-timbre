(ns slf4j-timbre.static-logger-binder
	(:gen-class
		:name org.slf4j.impl.StaticLoggerBinder
		:implements [org.slf4j.spi.LoggerFactoryBinder]
		:factory getSingleton
		:init init)
	(:import com.github.fzakaria.slf4j.timbre.TimbreLoggerFactory))

(def ^:private singleton-instance
	(atom nil))

(defn -init
	[]
	(compare-and-set! singleton-instance nil (TimbreLoggerFactory.))
	[[]])

(defn -getLoggerFactory
	[_]
	@singleton-instance)

(defn -getLoggerFactoryClassStr
	[_]
	(.getName TimbreLoggerFactory))