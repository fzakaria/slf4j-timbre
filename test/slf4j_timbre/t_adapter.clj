(ns slf4j-timbre.t-adapter
	(:require
		[taoensso.timbre :as timbre]
		slf4j-timbre.adapter)
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
				(timbre/with-context {:foo "preserved"}
					(invoke-each logger ?args)
					(invoke-each logger marker ?args)) => anything ; for side effects only

				(count @log-entries) => 10
				(map :level @log-entries) => (contains [:error :warn :info :debug :trace] :in-any-order)
				(map :context @log-entries) => (has some #{{:foo "preserved"}})
				(map :context @log-entries) => (has some #{{:foo "preserved" :marker "marker1"}})

				@log-entries => (has every? (comp #{(str *ns*)} :?ns-str))
				@log-entries => (has every? (comp #{"t_adapter.clj"} :?file))
				@log-entries => (has every? (comp pos? :?line))
				@log-entries => (has every? (comp #{"one two three four"} force :msg_)))

			?args
			["one two three four"]
			["one two three {}" "four"]
			["one two {} {}" "three" "four"]
			["one {} {} {}" (to-array ["two" "three" "four"])]
			["one two three four" (Exception. "test")]
			["one two three four" (identity nil)]
			["one two {} {}" (to-array ["three" "four" (Exception. "test")])]
			)

		(tabular
			(facts
				(timbre/with-context {:foo "preserved"}
					(invoke-each-lal logger nil "slf4j_timbre.t_adapter" ?message (to-array ?arg-array) ?t)
					(invoke-each-lal logger marker "slf4j_timbre.t_adapter" ?message (to-array ?arg-array) ?t)) => anything ; for side effects only

				(count @log-entries) => 10
				(map :level @log-entries) => (contains [:error :warn :info :debug :trace] :in-any-order)
				(map :context @log-entries) => (has some #{{:foo "preserved"}})
				(map :context @log-entries) => (has some #{{:foo "preserved" :marker "marker1"}})

				@log-entries => (has every? (comp #{(str *ns*)} :?ns-str))
				@log-entries => (has every? (comp pos? :?line))
				@log-entries => (has every? (comp #{"one two three four"} force :msg_)))

			?message             ?arg-array             ?t
			"one two three four" nil                    nil
			"one two three {}"   ["four"]               nil
			"one two {} {}"      ["three" "four"]       nil
			"one {} {} {}"       ["two" "three" "four"] nil
			"one two three four" nil                    (Exception. "test")
			"one two {} {}"      ["three" "four"]       (Exception. "test")
			)))