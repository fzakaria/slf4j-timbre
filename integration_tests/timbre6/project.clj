(defproject timbre6 "0.1.0-SNAPSHOT"
  :dependencies [[org.clojure/clojure "1.11.1"]
                 [com.taoensso/timbre "6.1.0"]
                 [emitter-slf4j17 "0.1.0-SNAPSHOT"]
                 [emitter-slf4j17-java "0.1.0-SNAPSHOT"]
                 [emitter-slf4j20 "0.1.0-SNAPSHOT"]
                 [emitter-slf4j20-java "0.1.0-SNAPSHOT"]
                 ; slf4j-timbre will be added here by lein update-in
                 ]
  :main timbre6.core
  :aot [timbre6.core])
