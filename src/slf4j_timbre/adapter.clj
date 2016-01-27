(ns slf4j-timbre.adapter
	(:gen-class
		:name         com.github.fzakaria.slf4j.timbre.TimbreLoggerAdapter
		:implements   [org.slf4j.Logger]
		:state        state
		:init         init
		:constructors {[String] []})
	(:require [taoensso.timbre :as timbre])
	(:import
		[org.slf4j.helpers FormattingTuple MessageFormatter]
		org.slf4j.Marker))

(defn -init
	[logger-name]
	[[] logger-name])

(defn -getName
	[this]
	(.state this))

(defmacro ^:private wrap
	[level checker]
	`(fn [this# & args#]
		(letfn [(inner#
				([msg#]
					(timbre/log-call ~level :p [msg#]))
				([msg# o1# o2#]
					(let [ft# (MessageFormatter/format msg# o1# o2#)]
						(if-let [t# (.getThrowable ft#)]
							(timbre/log-call ~level :p [t# (.getMessage ft#)])
							(timbre/log-call ~level :p [(.getMessage ft#)]))))
				([msg# o#]
					(cond
						(string? o#)
							(let [ft# (MessageFormatter/format msg# o#)]
								(if-let [t# (.getThrowable ft#)]
									(timbre/log-call ~level :p [t# (.getMessage ft#)])
									(timbre/log-call ~level :p [(.getMessage ft#)])))
						(.isArray (class o#))
							(let [ft# (MessageFormatter/arrayFormat msg# o#)]
								(if-let [t# (.getThrowable ft#)]
									(timbre/log-call ~level :p [t# (.getMessage ft#)])
									(timbre/log-call ~level :p [(.getMessage ft#)])))
						(isa? (class o#) Throwable)
							(timbre/log-call ~level :p [o# msg#]))))]
			(when (~checker this#)
				(if (isa? (class (first args#)) Marker)
					(timbre/with-context {:marker (.getName (first args#))} (apply inner# (rest args#)))
					(apply inner# args#))))))

(defn -isErrorEnabled
	([_]   (timbre/log? :error))
	([_ _] (timbre/log? :error)))
(defn -isWarnEnabled
	([_]   (timbre/log? :warn))
	([_ _] (timbre/log? :warn)))
(defn -isInfoEnabled
	([_]   (timbre/log? :info))
	([_ _] (timbre/log? :info)))
(defn -isDebugEnabled
	([_]   (timbre/log? :debug))
	([_ _] (timbre/log? :debug)))
(defn -isTraceEnabled
	([_]   (timbre/log? :trace))
	([_ _] (timbre/log? :trace)))

(def -error (wrap :error -isErrorEnabled))
(def -warn  (wrap :warn  -isWarnEnabled))
(def -info  (wrap :info  -isInfoEnabled))
(def -debug (wrap :debug -isDebugEnabled))
(def -trace (wrap :trace -isTraceEnabled))