(ns slf4j-timbre.adapter
	(:gen-class
		:name com.github.fzakaria.slf4j.timbre.TimbreLoggerAdapter
		:implements [org.slf4j.Logger])
	(:require [taoensso.timbre :as timbre])
	(:import
		[org.slf4j.helpers FormattingTuple MessageFormatter]
		org.slf4j.Marker))

(defmacro ^:private wrap
	[method]
	`(fn [_# & args#]
		(letfn [(inner#
				([msg#]
					(~method msg#))
				([msg# o1# o2#]
					(let [ft# (MessageFormatter/format msg# o1# o2#)]
						(~method (.getThrowable ft#) (.getMessage ft#))))
				([msg# o#]
					(cond
						(string? o#)
							(let [ft# (MessageFormatter/format msg# o#)]
								(~method (.getThrowable ft#) (.getMessage ft#)))
						(.isArray (class o#))
							(let [ft# (MessageFormatter/arrayFormat msg# o#)]
								(~method (.getThrowable ft#) (.getMessage ft#)))
						(isa? (class o#) Throwable)
							(~method o# msg#))))]
			(if (isa? (class (first args#)) Marker)
				(apply inner# (rest args#))
				(apply inner# args#)))))

(def -error (wrap timbre/error))
(def -warn  (wrap timbre/warn))
(def -info  (wrap timbre/info))
(def -debug (wrap timbre/debug))
(def -trace (wrap timbre/trace))

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