(defproject com.fzakaria/slf4j-timbre "0.2.1"
  :description "SLF4J binding for Timbre"
  :url "https://github.com/fzakaria/slf4j-timbre"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.7.0"]
                 [com.taoensso/timbre "4.1.4"]
                 [org.slf4j/slf4j-api "1.7.12"]
                 [junit/junit "4.12"]]

  :plugins [[lein-ancient "0.6.5"]
            [lein-junit "1.1.8"]]

  :source-paths ["src/main/clj"]
  :test-paths ["src/test/clj"]

  :java-source-paths ["src/main/java" "src/test/java"]

  :junit ["test/java"]

  :prep-tasks ["compile" "javac" "compile"]

  :aot :all

  :scm {:name "git"
        :url "https://github.com/fzakaria/slf4j-timbre"}

  )