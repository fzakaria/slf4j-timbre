(ns slf4j-timbre.adapter
  (:gen-class
   :name com.github.fzakaria.slf4j.timbre.TimbreLoggerAdapterHelper
   :methods [[isTraceEnabled [] boolean]
             [trace [String] void]
             [trace [String Throwable] void]
             [isDebugEnabled [] boolean]
             [debug [String] void]
             [debug [String Throwable] void]
             [isInfoEnabled [] boolean]
             [info [String] void]
             [info [String Throwable] void]
             [isWarnEnabled [] boolean]
             [warn [String] void]
             [warn [String Throwable] void]
             [isErrorEnabled [] boolean]
             [error [String] void]
             [error [String Throwable] void]])
  (:require  [taoensso.timbre :as timbre]))

(defn hello
  []
  (println "hello"))

(defn -isTraceEnabled
  [this]
  (timbre/log? :trace))

(defn -trace
  ([this msg]
   (timbre/trace msg))
  ([this msg throwable]
    (timbre/trace throwable msg)))

(defn -isDebugEnabled
  [this]
  (timbre/log? :debug))

(defn -debug
  ([this msg]
   (timbre/debug msg))
  ([this msg throwable]
   (timbre/debug throwable msg)))

(defn -isInfoEnabled
  [this]
  (timbre/log? :info))

(defn -info
  ([this msg]
   (timbre/info msg))
  ([this msg throwable]
   (timbre/info throwable msg)))

(defn -isWarnEnabled
  [this]
  (timbre/log? :warn))

(defn -warn
  ([this msg]
   (timbre/warn msg))
  ([this msg throwable]
   (timbre/warn throwable msg)))

(defn -isErrorEnabled
  [this]
  (timbre/log? :error))

(defn -error
  ([this msg]
   (timbre/error msg))
  ([this msg throwable]
   (timbre/error throwable msg)))