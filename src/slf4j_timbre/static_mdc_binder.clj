(ns slf4j-timbre.static-mdc-binder
	(:gen-class
		:name org.slf4j.impl.StaticMDCBinder
		:factory getSingleton
		:methods
			[[getMDCA [] org.slf4j.spi.MDCAdapter]
			 [getMDCAdapterClassStr [] String]])
	(:import org.slf4j.helpers.NOPMDCAdapter))

(defn -getMDCA
	[_]
	(NOPMDCAdapter.))

(defn -getMDCAdapterClassStr
	[_]
	(.getName NOPMDCAdapter))