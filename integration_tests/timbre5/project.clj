(defproject example "1.0.0"
  :dependencies [[org.clojure/clojure "1.11.1"]
                 [com.taoensso/timbre "5.2.1"]
                 [org.slf4j/slf4j-api "1.7.36"]
                 ; slf4j-timbre will be added here by lein update-in
                 ]
  :main example.core
  :aot [example.core])