(ns slf4j-timbre.adapter
	(:gen-class
		:name         com.github.fzakaria.slf4j.timbre.TimbreLoggerAdapter
		:implements   [org.slf4j.spi.LocationAwareLogger]
		:state        state
		:init         init
		:constructors {[String] []})
	(:require
		[taoensso.timbre :as timbre])
	(:import
		[org.slf4j.helpers FormattingTuple MessageFormatter]
		org.slf4j.Marker
		org.slf4j.spi.LocationAwareLogger))

(defn -init
	[logger-name]
	[[] logger-name])

(defn -getName
	[this]
	(.state this))

(defn- identify-caller
	[fqcn stack]
	(second
		(drop-while
			#(not= fqcn (.getClassName %))
			stack)))

(defmacro define-methods
	"Defines the various overloads for a given logging method (e.g., -info).
	Several have the same arity so we use an undocumented Clojure feature [1] to specify their type signatures.
	This macro expands into a (do ...) sexpr containing a defn for each of the ten variants declared in the Logger interface.
	[1] https://groups.google.com/d/embed/msg/clojure/KmNbLo8xTSs/d1Rs3Cs6DbAJ"
	[method-name level]
	`(do
		~@(for [signature    ["-String" "-String-Object" "-String-Object-Object" "-String-Object<>" "-String-Throwable"]
		        with-marker? [false true]]
			(let [func-sym   (symbol (str method-name (when with-marker? "-Marker") signature))
			      args-sym   (gensym "args")
			      ns-str-sym (gensym "ns-str")
			      file-sym   (gensym "file")
			      line-sym   (gensym "line")]

				`(defn ~func-sym [this# & ~args-sym]
					(when (timbre/log? ~level)
						(let [context#    ~(when with-marker? `(when-let [marker# (first ~args-sym)] {:marker (.getName marker#)}))
						      ; we do a nil check above because log4j-over-slf4j passes a null Marker instead of calling the correct (Marker-free) method
						      ~args-sym   ~(if with-marker? `(rest ~args-sym) args-sym)
						      stack#      (.getStackTrace (Thread/currentThread))
						      caller#     (identify-caller (.getName (.getClass this#)) stack#)
						      ~ns-str-sym (.getName this#)
						      ~file-sym   (.getFileName caller#)
						      ~line-sym   (.getLineNumber caller#)]
							(timbre/with-context context#
								~(case signature
									"-String"
									`(let [[msg#] ~args-sym]
										(timbre/log! ~level :p [msg#] {:?ns-str ~ns-str-sym :?file ~file-sym :?line ~line-sym}))

									"-String-Object"
									`(let [[fmt# o#] ~args-sym
									       ft# (MessageFormatter/format fmt# o#)]
										(if-let [t# (.getThrowable ft#)]
											(timbre/log! ~level :p [t# (.getMessage ft#)] {:?ns-str ~ns-str-sym :?file ~file-sym :?line ~line-sym})
											(timbre/log! ~level :p [   (.getMessage ft#)] {:?ns-str ~ns-str-sym :?file ~file-sym :?line ~line-sym})))

									"-String-Object-Object"
									`(let [[fmt# o1# o2#] ~args-sym
									       ft# (MessageFormatter/format fmt# o1# o2#)]
										(if-let [t# (.getThrowable ft#)]
											(timbre/log! ~level :p [t# (.getMessage ft#)] {:?ns-str ~ns-str-sym :?file ~file-sym :?line ~line-sym})
											(timbre/log! ~level :p [   (.getMessage ft#)] {:?ns-str ~ns-str-sym :?file ~file-sym :?line ~line-sym})))

									"-String-Object<>"
									`(let [[fmt# os#] ~args-sym
									       ft# (MessageFormatter/arrayFormat fmt# os#)]
										(if-let [t# (.getThrowable ft#)]
											(timbre/log! ~level :p [t# (.getMessage ft#)] {:?ns-str ~ns-str-sym :?file ~file-sym :?line ~line-sym})
											(timbre/log! ~level :p [   (.getMessage ft#)] {:?ns-str ~ns-str-sym :?file ~file-sym :?line ~line-sym})))

									"-String-Throwable"
									`(let [[msg# t#] ~args-sym]
										(timbre/log! ~level :p [t# msg#] {:?ns-str ~ns-str-sym :?file ~file-sym :?line ~line-sym})))))))))))


(define-methods "-error" :error)
(define-methods "-warn"  :warn)
(define-methods "-info"  :info)
(define-methods "-debug" :debug)
(define-methods "-trace" :trace)

(defn -log
	[this marker fqcn level message arg-array t]
	(assert (nil? arg-array))
	(let [levels {LocationAwareLogger/ERROR_INT :error
	              LocationAwareLogger/WARN_INT  :warn
	              LocationAwareLogger/INFO_INT  :info
	              LocationAwareLogger/DEBUG_INT :debug
	              LocationAwareLogger/TRACE_INT :trace}]
		(when (timbre/log? (levels level))
			(let [stack  (.getStackTrace (Thread/currentThread))
			      caller (identify-caller fqcn stack)]
				(timbre/with-context (when marker {:marker (.getName marker)})
					(timbre/log! (levels level) :p
						[t message]
						{:?ns-str (.getName this)
						 :?file   (.getFileName caller)
						 :?line   (.getLineNumber caller)}))))))

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