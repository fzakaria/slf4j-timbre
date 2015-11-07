(ns slf4j-timbre.adapter
	(:gen-class
		:name com.github.fzakaria.slf4j.timbre.TimbreLoggerAdapter
		:extends org.slf4j.helpers.MarkerIgnoringBase)
	(:require [taoensso.timbre :as timbre])
	(:import [org.slf4j.helpers FormattingTuple MarkerIgnoringBase MessageFormatter]))

(defn -isErrorEnabled
	[this]
	(timbre/log? :error))

(defn -error
	([this msg]
		(timbre/error msg))
	([this msg o1 o2]
		(let [ft (MessageFormatter/format msg o1 o2)]
			(timbre/error (.getThrowable ft) (.getMessage ft))))
	([this msg o]
		(cond
			(string? o)
				(let [ft (MessageFormatter/format msg o)]
					(timbre/error (.getThrowable ft) (.getMessage ft)))
			(.isArray (class o))
				(let [ft (MessageFormatter/arrayFormat msg o)]
					(timbre/error (.getThrowable ft) (.getMessage ft)))
			(isa? (class o) Throwable)
				(timbre/error o msg))))

(defn -isWarnEnabled
	[this]
	(timbre/log? :warn))

(defn -warn
	([this msg]
		(timbre/warn msg))
	([this msg o1 o2]
		(let [ft (MessageFormatter/format msg o1 o2)]
			(timbre/warn (.getThrowable ft) (.getMessage ft))))
	([this msg o]
		(cond
			(string? o)
				(let [ft (MessageFormatter/format msg o)]
					(timbre/warn (.getThrowable ft) (.getMessage ft)))
			(.isArray (class o))
				(let [ft (MessageFormatter/arrayFormat msg o)]
					(timbre/warn (.getThrowable ft) (.getMessage ft)))
			(isa? (class o) Throwable)
				(timbre/warn o msg))))

(defn -isInfoEnabled
	[this]
	(timbre/log? :info))

(defn -info
	([this msg]
		(timbre/info msg))
	([this msg o1 o2]
		(let [ft (MessageFormatter/format msg o1 o2)]
			(timbre/info (.getThrowable ft) (.getMessage ft))))
	([this msg o]
		(cond
			(string? o)
				(let [ft (MessageFormatter/format msg o)]
					(timbre/info (.getThrowable ft) (.getMessage ft)))
			(.isArray (class o))
				(let [ft (MessageFormatter/arrayFormat msg o)]
					(timbre/info (.getThrowable ft) (.getMessage ft)))
			(isa? (class o) Throwable)
				(timbre/info o msg))))

(defn -isDebugEnabled
	[this]
	(timbre/log? :debug))

(defn -debug
	([this msg]
		(timbre/debug msg))
	([this msg o1 o2]
		(let [ft (MessageFormatter/format msg o1 o2)]
			(timbre/debug (.getThrowable ft) (.getMessage ft))))
	([this msg o]
		(cond
			(string? o)
				(let [ft (MessageFormatter/format msg o)]
					(timbre/debug (.getThrowable ft) (.getMessage ft)))
			(.isArray (class o))
				(let [ft (MessageFormatter/arrayFormat msg o)]
					(timbre/debug (.getThrowable ft) (.getMessage ft)))
			(isa? (class o) Throwable)
				(timbre/debug o msg))))

(defn -isTraceEnabled
	[this]
	(timbre/log? :trace))

(defn -trace
	([this msg]
		(timbre/trace msg))
	([this msg o1 o2]
		(let [ft (MessageFormatter/format msg o1 o2)]
			(timbre/trace (.getThrowable ft) (.getMessage ft))))
	([this msg o]
		(cond
			(string? o)
				(let [ft (MessageFormatter/format msg o)]
					(timbre/trace (.getThrowable ft) (.getMessage ft)))
			(.isArray (class o))
				(let [ft (MessageFormatter/arrayFormat msg o)]
					(timbre/trace (.getThrowable ft) (.getMessage ft)))
			(isa? (class o) Throwable)
				(timbre/trace o msg))))