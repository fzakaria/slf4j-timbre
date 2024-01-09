(defproject com.fzakaria/slf4j-timbre "lein-git-inject/version"
  :description "SLF4J binding for Timbre"
  :url "https://github.com/fzakaria/slf4j-timbre"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.11.1"]
                 [com.taoensso/timbre "6.3.1"]
                 [org.slf4j/slf4j-api "2.0.11"]]
  :profiles {:dev
             {:dependencies [[midje "1.10.9"]]
              :plugins [[lein-midje "3.2.2"]
                        [lein-sub "0.3.0"]
                        [day8/lein-git-inject "0.0.15"]]
              :sub ["integration_tests/emitter-slf4j17"
                    "integration_tests/emitter-slf4j17-java"
                    "integration_tests/emitter-slf4j20"
                    "integration_tests/emitter-slf4j20-java"]
              :aliases {"run-integration-tests" ["do" "sub" "install,"
                                                      "sub" "-s" "integration_tests/timbre6" "update-in" ":dependencies" "conj" "[com.fzakaria/slf4j-timbre \"lein-git-inject/version\"]" "--" "run"]}}}
  :middleware [leiningen.git-inject/middleware]
  :git-inject {:version-pattern #"^(\d+\.\d+\.\d+)$"}
  :aot [slf4j-timbre.adapter
        slf4j-timbre.factory
        slf4j-timbre.static-logger-binder
        slf4j-timbre.static-marker-binder
        slf4j-timbre.static-mdc-binder
        slf4j-timbre.service-provider]
  :jar-exclusions [#"\.class$"]
  :jar-inclusions [#"slf4j.*\.class$"]
  :release-tasks [["vcs" "assert-committed"]
                  ["deploy" "clojars"]
                  ["vcs" "push"]]
  :scm {:name "git"
        :url "https://github.com/fzakaria/slf4j-timbre"})