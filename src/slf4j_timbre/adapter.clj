(ns slf4j-timbre.adapter
	(:gen-class
		:name         com.github.fzakaria.slf4j.timbre.TimbreLoggerAdapter
		:implements   [org.slf4j.spi.LocationAwareLogger]
		:state        state
		:init         init
		:constructors {[String] []})
	(:require
		clojure.string
		[taoensso.timbre :as timbre])
	(:import
		org.slf4j.helpers.MessageFormatter
		org.slf4j.spi.LocationAwareLogger))

(defn -init
	[logger-name]
	[[] logger-name])

(defn -getName
	[this]
	(.state this))

(defn- identify-caller
	[fqcn stack]
	(letfn [(matches [el] (= 0 (clojure.string/index-of (str (.getClassName el) "$") (str fqcn "$"))))]
		(->> stack
			(drop-while (comp not matches))
			(drop-while matches)
			(first))))

(defn- assoc-if-map
	"assoc only if m is a map or nil"
	[m k v]
	(if (or (nil? m) (map? m))
		(assoc m k v)
		m))

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
					(when (timbre/may-log? ~level)
						(let [context#    ~(if with-marker? `(if-let [marker# (first ~args-sym)] (assoc-if-map timbre/*context* :marker (.getName marker#)) timbre/*context*) `timbre/*context*)
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


; The log level we pass to timbre/log! needs to be a compile-time constant so timbre can perform compile-time elision.
; To achieve this we make -log a multimethod which dispatches based on its level argument (e.g. LocationAwareLogger/ERROR_INT)
; and then define a separate method for each log level which passes the corresponding keyword (e.g. :error) - a compile-time constant - to timbre.

(defmulti -log
	"-log method of LocationAwareLogger interface used by log4j-over-slf4j etc"
	(fn [_ _ _ level _ _ _] level))

(defmacro define-log-method
	[level-const level-keyword]
	`(defmethod -log ~level-const
		[this# marker# fqcn# _# fmt# arg-array# t#]
		(when (timbre/may-log? ~level-keyword)
			(let [stack#   (.getStackTrace (Thread/currentThread))
			      caller#  (identify-caller fqcn# stack#)
			      message# (.getMessage (MessageFormatter/arrayFormat fmt# arg-array#))]
				(timbre/with-context (if marker# (assoc-if-map timbre/*context* :marker (.getName marker#)) timbre/*context*)
					(if caller# ; nil when fqcn provided is incorrect (not present in call stack)
						(if t#
							(timbre/log! ~level-keyword :p
								[t# message#]
								{:?ns-str (.getName this#)
								 :?file   (.getFileName caller#)
								 :?line   (.getLineNumber caller#)})
							(timbre/log! ~level-keyword :p
								[message#]
								{:?ns-str (.getName this#)
								 :?file   (.getFileName caller#)
								 :?line   (.getLineNumber caller#)}))
						(if t#
							(timbre/log! ~level-keyword :p [t# message#] {:?ns-str (.getName this#)})
							(timbre/log! ~level-keyword :p    [message#] {:?ns-str (.getName this#)}))))))))

(define-log-method LocationAwareLogger/ERROR_INT :error)
(define-log-method LocationAwareLogger/WARN_INT  :warn)
(define-log-method LocationAwareLogger/INFO_INT  :info)
(define-log-method LocationAwareLogger/DEBUG_INT :debug)
(define-log-method LocationAwareLogger/TRACE_INT :trace)


(defn -isErrorEnabled
	([_]   (boolean (timbre/may-log? :error)))
	([_ _] (boolean (timbre/may-log? :error))))
(defn -isWarnEnabled
	([_]   (boolean (timbre/may-log? :warn)))
	([_ _] (boolean (timbre/may-log? :warn))))
(defn -isInfoEnabled
	([_]   (boolean (timbre/may-log? :info)))
	([_ _] (boolean (timbre/may-log? :info))))
(defn -isDebugEnabled
	([_]   (boolean (timbre/may-log? :debug)))
	([_ _] (boolean (timbre/may-log? :debug))))
(defn -isTraceEnabled
	([_]   (boolean (timbre/may-log? :trace)))
	([_ _] (boolean (timbre/may-log? :trace))))