(ns slf4j-timbre.adapter
	(:gen-class
		:name         com.github.fzakaria.slf4j.timbre.TimbreLoggerAdapter
		:implements   [org.slf4j.Logger]
		:state        state
		:init         init
		:constructors {[String] []})
	(:require
		[taoensso.timbre :as timbre])
	(:import
		[org.slf4j.helpers FormattingTuple MessageFormatter]
		org.slf4j.Marker))

(defn -init
	[logger-name]
	[[] logger-name])

(defn -getName
	[this]
	(.state this))

(defn make-log-fn
	[level {:keys [?ns-str ?file ?line]}]
	(fn
		([msg]
			(timbre/log-call level :p [msg] {:?ns-str ?ns-str :?file ?file :?line ?line}))
		([msg o1 o2]
			(let [ft (MessageFormatter/format msg o1 o2)]
				(if-let [t (.getThrowable ft)]
					(timbre/log-call level :p [t (.getMessage ft)] {:?ns-str ?ns-str :?file ?file :?line ?line})
					(timbre/log-call level :p [  (.getMessage ft)] {:?ns-str ?ns-str :?file ?file :?line ?line}))))
		([msg o]
			(cond
				(string? o)
					(let [ft (MessageFormatter/format msg o)]
						(if-let [t (.getThrowable ft)]
							(timbre/log-call level :p [t (.getMessage ft)] {:?ns-str ?ns-str :?file ?file :?line ?line})
							(timbre/log-call level :p [  (.getMessage ft)] {:?ns-str ?ns-str :?file ?file :?line ?line})))
				(.isArray (class o))
					(let [ft (MessageFormatter/arrayFormat msg o)]
						(if-let [t (.getThrowable ft)]
							(timbre/log-call level :p [t (.getMessage ft)] {:?ns-str ?ns-str :?file ?file :?line ?line})
							(timbre/log-call level :p [  (.getMessage ft)] {:?ns-str ?ns-str :?file ?file :?line ?line})))
				(isa? (class o) Throwable)
					(timbre/log-call level :p [o msg] {:?ns-str ?ns-str :?file ?file :?line ?line})))))

(defmacro ^:private wrap
	[level]
	`(fn [this# & args#]
		(when (timbre/log? ~level)
			(let
				[stack#  (.getStackTrace (Thread/currentThread))
				 caller# (second (drop-while #(not= (.getName (.getClass this#)) (.getClassName %)) stack#))
				 opts#
					{:?ns-str (.getName this#)
					 :?file   (.getFileName caller#)
					 :?line   (.getLineNumber caller#)}
				 log# (make-log-fn ~level opts#)]
				(if (isa? (class (first args#)) Marker)
					(timbre/with-context {:marker (.getName (first args#))}
						(apply log# (rest args#)))
					(apply log# args#))))))

(def -error (wrap :error))
(def -warn  (wrap :warn))
(def -info  (wrap :info))
(def -debug (wrap :debug))
(def -trace (wrap :trace))

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