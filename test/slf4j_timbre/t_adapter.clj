(ns slf4j-timbre.t-adapter
	(:require
		[taoensso.timbre :as timbre]
		[slf4j-timbre.adapter])
	(:use midje.sweet)
	(:import org.slf4j.spi.LocationAwareLogger))


(defmacro invoke-each
	"Like (do (apply .error arg-1 arg-2 args-rest) (apply .warn arg-1 arg-2 args-rest) etc for each log level"
	[& body]
	(let [args (butlast body) args-rest (last body)]
		`(do
			~@(for [m '[.error .warn .info .debug .trace]]
				`(~m ~@args ~@args-rest)))))

(defn invoke-each-lal
	[logger marker fqcn message arg-array t]
	(dorun
		(for [level [LocationAwareLogger/ERROR_INT LocationAwareLogger/WARN_INT LocationAwareLogger/INFO_INT LocationAwareLogger/DEBUG_INT LocationAwareLogger/TRACE_INT]]
			(.log logger marker fqcn level message arg-array t))))

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
			["Hello World" (identity nil)]
			["Hello World {} {}" (to-array ["Farid" "Zakaria" (Exception. "test")])]
			)

		(tabular
			(facts
				(invoke-each-lal logger nil "slf4j_timbre.t_adapter" ?message (to-array ?arg-array) ?t) => anything ; for side effects only
				(invoke-each-lal logger marker "slf4j_timbre.t_adapter" ?message (to-array ?arg-array) ?t) => anything ; for side effects only

				(count @log-entries) => 10
				(map :level @log-entries) => (contains [:error :warn :info :debug :trace] :in-any-order)

				@log-entries => (has every? (comp #{"slf4j-timbre.t-adapter"} :?ns-str))
				@log-entries => (has every? (comp #{"t_adapter.clj"} :?file))
				@log-entries => (has every? (comp pos? :?line)))

			?message               ?arg-array            ?t
			"Hello world"          nil                   nil
			"Hello world {}"       ["one"]               nil
			"Hello world {} {}"    ["one" "two"]         nil
			"Hello world {} {} {}" ["one" "two" "three"] nil
			"Hello world"          nil                   (Exception. "test")
			"Hello world {} {}"    ["one" "two"]         (Exception. "test")
			)))