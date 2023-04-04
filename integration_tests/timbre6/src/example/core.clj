(ns example.core
  (:require [taoensso.timbre :as timbre])
  (:import org.slf4j.LoggerFactory)
  (:gen-class))

(defn -main
  [& _]
  (timbre/error "Hello from Timbre")
  (timbre/warn "Hello from Timbre")
  (timbre/info "Hello from Timbre")
  (timbre/debug "Hello from Timbre")
  (timbre/trace "Hello from Timbre")
  (let [logger (LoggerFactory/getLogger (str *ns*))]
    (.error logger "Hello from SLF4J")
    (.warn logger "Hello from SLF4J")
    (.info logger "Hello from SLF4J")
    (.debug logger "Hello from SLF4J")
    (.trace logger "Hello from SLF4J")))