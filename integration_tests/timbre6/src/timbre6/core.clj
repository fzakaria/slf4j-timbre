(ns timbre6.core
  (:require [taoensso.timbre :as timbre])
  (:import [com.example Slf4j17Emitter Slf4j17JavaEmitter Slf4j20Emitter Slf4j20JavaEmitter]))

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
  (Slf4j20JavaEmitter/emit))
