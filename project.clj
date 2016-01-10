(defproject com.fzakaria/slf4j-timbre "0.2.3"
  :description "SLF4J binding for Timbre"
  :url "https://github.com/fzakaria/slf4j-timbre"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.7.0"]
                 [com.taoensso/timbre "4.2.0"]
                 [org.clojure/tools.reader "1.0.0-alpha2"]]

  :plugins [[lein-ancient "0.6.5"]]

  :profiles {:dev {:dependencies [[midje "1.8.1"]
                                  [org.slf4j/slf4j-api "1.7.13"]]
                   :plugins [[lein-midje "3.2"]]}}

  :aot :all

  :scm {:name "git"
        :url "https://github.com/fzakaria/slf4j-timbre"}

  )
