(ns slf4j-timbre.t-adapter
	(:require
		[taoensso.timbre :as timbre]
		[slf4j-timbre.adapter])
	(:use midje.sweet))


(defmacro invoke-each
	"Like (do (apply .error arg-1 arg-2 args-rest) (apply .warn arg-1 arg-2 args-rest) etc for each log level"
	[& body]
	(let [args (butlast body) args-rest (last body)]
		`(do
			~@(for [m '[.error .warn .info .debug .trace]]
				`(~m ~@args ~@args-rest)))))


(def log-entries
	(atom []))

(timbre/set-config!
	{:level :trace :appenders {:counter {:enabled? true :fn (fn [data] (swap! log-entries conj data))}}})


(let [logger (org.slf4j.LoggerFactory/getLogger (str *ns*))
      marker (org.slf4j.MarkerFactory/getMarker "marker1")]

	(with-state-changes [(before :facts (reset! log-entries []))]

		(tabular
			(facts
				(invoke-each logger ?args) => anything ; for side effects only
				(invoke-each logger marker ?args) => anything ; for side effects only

				(count @log-entries) => 10
				(map :level @log-entries) => (contains [:error :warn :info :debug :trace] :in-any-order)

				@log-entries => (has every? (comp #{"slf4j-timbre.t-adapter"} :?ns-str))
				@log-entries => (has every? (comp #{"t_adapter.clj"} :?file))
				@log-entries => (has every? (comp pos? :?line)))

			?args
			["Hello World"]
			["Hello World {}" "Farid"]
			["Hello World {} {}" "Farid" "Zakaria"]
			["Hello World {} {} {}" (to-array ["What" "a" "Beautiful Day!"])]
			["Hello World" (Exception. "test")]
			["Hello World {} {}" (to-array ["Farid" "Zakaria" (Exception. "test")])]
			)))