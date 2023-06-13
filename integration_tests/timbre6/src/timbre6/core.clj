(ns timbre6.core
  (:require [taoensso.timbre :as timbre])
  (:import org.slf4j.bridge.SLF4JBridgeHandler
           [com.example Slf4j17Emitter Slf4j17JavaEmitter Slf4j20Emitter Slf4j20JavaEmitter Log4j2JavaEmitter JclJavaEmitter JulJavaEmitter]))

(defn ^:private emit []
  (timbre/error "Hello from Timbre")
  (timbre/warn "Hello from Timbre")
  (timbre/info "Hello from Timbre")
  (timbre/debug "Hello from Timbre")
  (timbre/trace "Hello from Timbre"))

(defn -main
  [& _]
  (emit)
  (Slf4j17Emitter/emit)
  (Slf4j17JavaEmitter/emit)
  (Slf4j20Emitter/emit)
  (Slf4j20JavaEmitter/emit)
  (Log4j2JavaEmitter/emit)
  (JclJavaEmitter/emit)
  (SLF4JBridgeHandler/removeHandlersForRootLogger)
  (SLF4JBridgeHandler/install)
  (JulJavaEmitter/emit))
