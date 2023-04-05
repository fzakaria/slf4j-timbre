(ns emitter-slf4j20.core
  (:import org.slf4j.LoggerFactory)
  (:gen-class
     :name com.example.Slf4j20Emitter
     :methods [^:static [emit [] void]]))

(defn -emit []
  (let [logger (LoggerFactory/getLogger (str *ns*))]
    (.error logger "Hello from SLF4J 2.0")
    (.warn logger "Hello from SLF4J 2.0")
    (.info logger "Hello from SLF4J 2.0")
    (.debug logger "Hello from SLF4J 2.0")
    (.trace logger "Hello from SLF4J 2.0")))
