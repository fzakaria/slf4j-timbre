(ns slf4j-timbre.adapter
	(:gen-class
		:name com.github.fzakaria.slf4j.timbre.TimbreLoggerAdapter
		:extends org.slf4j.helpers.MarkerIgnoringBase)
	(:require [taoensso.timbre :as timbre])
	(:import [org.slf4j.helpers FormattingTuple MarkerIgnoringBase MessageFormatter]))

(defmacro ^:private wrap
	[method]
	`(fn
		([_# msg#]
			(~method msg#))
		([_# msg# o1# o2#]
			(let [ft# (MessageFormatter/format msg# o1# o2#)]
				(~method (.getThrowable ft#) (.getMessage ft#))))
		([_# msg# o#]
			(cond
				(string? o#)
					(let [ft# (MessageFormatter/format msg# o#)]
						(~method (.getThrowable ft#) (.getMessage ft#)))
				(.isArray (class o#))
					(let [ft# (MessageFormatter/arrayFormat msg# o#)]
						(~method (.getThrowable ft#) (.getMessage ft#)))
				(isa? (class o#) Throwable)
					(~method o# msg#)))))

(def -error (wrap timbre/error))
(def -warn  (wrap timbre/warn))
(def -info  (wrap timbre/info))
(def -debug (wrap timbre/debug))
(def -trace (wrap timbre/trace))

(defn -isErrorEnabled [_] (timbre/log? :error))
(defn -isWarnEnabled  [_] (timbre/log? :warn))
(defn -isInfoEnabled  [_] (timbre/log? :info))
(defn -isDebugEnabled [_] (timbre/log? :debug))
(defn -isTraceEnabled [_] (timbre/log? :trace))