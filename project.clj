(defproject com.fzakaria/slf4j-timbre "lein-git-inject/version"
  :description "SLF4J binding for Timbre"
  :url "https://github.com/fzakaria/slf4j-timbre"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.10.1"]
                 [com.taoensso/timbre "4.10.0"]
                 [org.slf4j/slf4j-api "1.7.30"]]
  :profiles {:dev
             {:dependencies [[midje "1.9.9"]]
              :plugins [[lein-midje "3.2.2"]
                        [lein-sub "0.3.0"]
                        [day8/lein-git-inject "0.0.14"]]
              :sub ["integration_tests/timbre4"
                    "integration_tests/timbre5"]
              :aliases {"run-integration-tests" ["sub" "do" "clean," "deps,"
                                                 "update-in" ":dependencies" "conj" "[com.fzakaria/slf4j-timbre \"lein-git-inject/version\"]" "--" "run"]}}}
  :middleware [leiningen.git-inject/middleware]
  :git-inject {:version-pattern #"^(\d+\.\d+\.\d+)$"}
  :aot [slf4j-timbre.adapter
        slf4j-timbre.factory
        slf4j-timbre.static-logger-binder
        slf4j-timbre.static-marker-binder
        slf4j-timbre.static-mdc-binder]
  :jar-exclusions [#"\.class$"]
  :jar-inclusions [#"slf4j.*\.class$"]
  :release-tasks [["vcs" "assert-committed"]
                  ["deploy"]
                  ["vcs" "commit" "Update pom.xml"] ; for dependabot
                  ["vcs" "push"]]
  :scm {:name "git"
        :url "https://github.com/fzakaria/slf4j-timbre"})