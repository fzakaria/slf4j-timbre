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
		com.github.fzakaria.slf4j.timbre.TimbreLoggerAdapter
		org.slf4j.Marker
		org.slf4j.helpers.MessageFormatter
		org.slf4j.spi.LocationAwareLogger))

(defn -init
	[logger-name]
	[[] logger-name])

(defn -getName
	[^TimbreLoggerAdapter this]
	(.state this))

(defn- identify-caller ^StackTraceElement
	[fqcn stack]
	(letfn [(matches [^StackTraceElement el] (= 0 (clojure.string/index-of (str (.getClassName el) "$") (str fqcn "$"))))]
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

				`(defn ~func-sym [^TimbreLoggerAdapter this# & ~args-sym]
					(when (timbre/may-log? ~level (.getName this#))
						(let [context#    ~(if with-marker? `(if-let [^Marker marker# (first ~args-sym)] (assoc-if-map timbre/*context* :marker (.getName marker#)) timbre/*context*) `timbre/*context*)
						      ; we check for a null Marker above to work around a bug in log4j-over-slf4j
						      ; see https://jira.qos.ch/projects/SLF4J/issues/SLF4J-432
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
	"-log method of LocationAwareLogger interface used by log4j-over-slf4j etc"
	[^TimbreLoggerAdapter this ^Marker marker fqcn level-const fmt arg-array t]
	(let [levels {LocationAwareLogger/ERROR_INT :error
	              LocationAwareLogger/WARN_INT  :warn
	              LocationAwareLogger/INFO_INT  :info
	              LocationAwareLogger/DEBUG_INT :debug
	              LocationAwareLogger/TRACE_INT :trace}
	      level-keyword (levels level-const)]
		(when (timbre/may-log? level-keyword (.getName this))
			(let [stack   (.getStackTrace (Thread/currentThread))
			      caller  (identify-caller fqcn stack)
			      message (.getMessage (MessageFormatter/arrayFormat fmt arg-array))]
				(timbre/with-context (if marker (assoc-if-map timbre/*context* :marker (.getName marker)) timbre/*context*)
					(if caller ; nil when fqcn provided is incorrect (not present in call stack)
						(if t
							(timbre/log! level-keyword :p
								[t message]
								{:?ns-str (.getName this)
								 :?file   (.getFileName caller)
								 :?line   (.getLineNumber caller)})
							(timbre/log! level-keyword :p
								[message]
								{:?ns-str (.getName this)
								 :?file   (.getFileName caller)
								 :?line   (.getLineNumber caller)}))
						(if t
							(timbre/log! ~level-keyword :p [t message] {:?ns-str (.getName this)})
							(timbre/log! ~level-keyword :p   [message] {:?ns-str (.getName this)}))))))))


(defn -isErrorEnabled
	([^TimbreLoggerAdapter this]   (boolean (timbre/may-log? :error (.getName this))))
	([^TimbreLoggerAdapter this _] (boolean (timbre/may-log? :error (.getName this)))))
(defn -isWarnEnabled
	([^TimbreLoggerAdapter this]   (boolean (timbre/may-log? :warn (.getName this))))
	([^TimbreLoggerAdapter this _] (boolean (timbre/may-log? :warn (.getName this)))))
(defn -isInfoEnabled
	([^TimbreLoggerAdapter this]   (boolean (timbre/may-log? :info (.getName this))))
	([^TimbreLoggerAdapter this _] (boolean (timbre/may-log? :info (.getName this)))))
(defn -isDebugEnabled
	([^TimbreLoggerAdapter this]   (boolean (timbre/may-log? :debug (.getName this))))
	([^TimbreLoggerAdapter this _] (boolean (timbre/may-log? :debug (.getName this)))))
(defn -isTraceEnabled
	([^TimbreLoggerAdapter this]   (boolean (timbre/may-log? :trace (.getName this))))
	([^TimbreLoggerAdapter this _] (boolean (timbre/may-log? :trace (.getName this)))))