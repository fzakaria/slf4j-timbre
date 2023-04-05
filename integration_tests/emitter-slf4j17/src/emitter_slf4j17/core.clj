(ns emitter-slf4j17.core
  (:import org.slf4j.LoggerFactory)
  (:gen-class
     :name com.example.Slf4j17Emitter
     :methods [^:static [emit [] void]]))

(defn -emit []
  (let [logger (LoggerFactory/getLogger (str *ns*))]
    (.error logger "Hello from SLF4J 1.7")
    (.warn logger "Hello from SLF4J 1.7")
    (.info logger "Hello from SLF4J 1.7")
    (.debug logger "Hello from SLF4J 1.7")
    (.trace logger "Hello from SLF4J 1.7")))
