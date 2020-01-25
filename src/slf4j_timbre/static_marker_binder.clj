(ns slf4j-timbre.static-marker-binder
	(:gen-class
		:name org.slf4j.impl.StaticMarkerBinder
		:implements [org.slf4j.spi.MarkerFactoryBinder]
		:factory getSingleton
		:init init)
	(:import org.slf4j.helpers.BasicMarkerFactory))

(def ^:private singleton-instance
	(atom nil))

(defn -init
	[]
	(compare-and-set! singleton-instance nil (BasicMarkerFactory.))
	[[]])

(defn -getMarkerFactory
	[_]
	@singleton-instance)

(defn -getMarkerFactoryClassStr
	[_]
	(.getName BasicMarkerFactory))