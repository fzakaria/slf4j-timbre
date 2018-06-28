(defproject com.fzakaria/slf4j-timbre "0.3.11"
	:description "SLF4J binding for Timbre"
	:url "https://github.com/fzakaria/slf4j-timbre"
	:license
		{:name "Eclipse Public License"
		 :url "http://www.eclipse.org/legal/epl-v10.html"}
	:dependencies
		[[org.clojure/clojure "1.9.0"]
		 [com.taoensso/timbre "4.10.0"]
		 [org.slf4j/slf4j-api "1.7.25"]]
	:profiles
		{:dev
			{:dependencies [[midje "1.9.1"]]
			 :plugins [[lein-midje "3.2.1"]]}}
	:aot
		[slf4j-timbre.adapter
		 slf4j-timbre.factory
		 slf4j-timbre.static-logger-binder
		 slf4j-timbre.static-marker-binder
		 slf4j-timbre.static-mdc-binder]
	:jar-exclusions [#"^((?!slf4j).)*\.class$"]
	:scm
		{:name "git"
		 :url "https://github.com/fzakaria/slf4j-timbre"})