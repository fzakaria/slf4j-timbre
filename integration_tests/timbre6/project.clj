(defproject timbre6 "0.1.0-SNAPSHOT"
  :dependencies [[org.clojure/clojure "1.11.1"]
                 [com.taoensso/timbre "6.1.0"]
                 [emitter-slf4j17 "0.1.0-SNAPSHOT"]
                 [emitter-slf4j17-java "0.1.0-SNAPSHOT"]
                 [emitter-slf4j20 "0.1.0-SNAPSHOT"]
                 [emitter-slf4j20-java "0.1.0-SNAPSHOT"]
                 [emitter-log4j2-java "0.1.0-SNAPSHOT"]
                 [emitter-jcl-java "0.1.0-SNAPSHOT"]
                 [emitter-jul-java "0.1.0-SNAPSHOT"]
                 [org.slf4j/log4j-over-slf4j "2.0.7"]
                 [org.slf4j/jcl-over-slf4j "2.0.7"]
                 [org.slf4j/jul-to-slf4j "2.0.7"]
                 ; slf4j-timbre will be added here by lein update-in
                 ]
  :main timbre6.core
  :aot [timbre6.core])
