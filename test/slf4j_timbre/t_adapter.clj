(ns slf4j-timbre.t-adapter
	(:require
		[taoensso.timbre :as timbre]
		[slf4j-timbre.adapter])
	(:use midje.sweet))

(def log-entries
	(atom []))

(timbre/set-config!
	{:level :trace :appenders {:counter {:enabled? true :fn (fn [data] (swap! log-entries conj data))}}})

(let [logger (org.slf4j.LoggerFactory/getLogger "basicLoggin")]

	(fact "simple string"
		(do
			(doto logger
				(.error "Hello World")
				(.warn "Hello World")
				(.info "Hello World")
				(.debug "Hello World")
				(.trace "Hello World"))
			(count @log-entries))
		=> 5)

	(fact "1 param"
		(do
			(doto logger
				(.error "Hello World {}" "Farid")
				(.warn "Hello World {}" "Farid")
				(.info "Hello World {}" "Farid")
				(.debug "Hello World {}" "Farid")
				(.trace "Hello World {}" "Farid"))
			(count @log-entries))
		=> 10)

	(fact "2 params"
		(do
			(doto logger
				(.error "Hello World {} {}" "Farid" "Zakaria")
				(.warn "Hello World {} {}" "Farid" "Zakaria")
				(.info "Hello World {} {}" "Farid" "Zakaria")
				(.debug "Hello World {} {}" "Farid" "Zakaria")
				(.trace "Hello World {} {}" "Farid" "Zakaria"))
			(count @log-entries))
		=> 15)

	(fact "3 params"
		(do
			(doto logger
				(.error "Hello World {} {} {}" (to-array ["What" "a" "Beautiful Day!"]))
				(.warn "Hello World {} {} {}" (to-array ["What" "a" "Beautiful Day!"]))
				(.info "Hello World {} {} {}" (to-array ["What" "a" "Beautiful Day!"]))
				(.debug "Hello World {} {} {}" (to-array ["What" "a" "Beautiful Day!"]))
				(.trace "Hello World {} {} {}" (to-array ["What" "a" "Beautiful Day!"])))
			(count @log-entries))
		=> 20)

	(fact "simple string + exception obj"
		(do
			(doto logger
				(.error "Hello World" (Exception. "test"))
				(.warn "Hello World" (Exception. "test"))
				(.info "Hello World" (Exception. "test"))
				(.debug "Hello World" (Exception. "test"))
				(.trace "Hello World" (Exception. "test")))
			(count @log-entries))
		=> 25)

	(fact "2 params + exception obj"
		(do
			(doto logger
				(.error "Hello World {} {}" (to-array ["Farid" "Zakaria" (Exception. "test")]))
				(.warn "Hello World {} {}" (to-array ["Farid" "Zakaria" (Exception. "test")]))
				(.info "Hello World {} {}" (to-array ["Farid" "Zakaria" (Exception. "test")]))
				(.debug "Hello World {} {}" (to-array ["Farid" "Zakaria" (Exception. "test")]))
				(.trace "Hello World {} {}" (to-array ["Farid" "Zakaria" (Exception. "test")])))
			(count @log-entries))
		=> 30)

	(fact "simple string with marker"
		(let [marker (org.slf4j.MarkerFactory/getMarker "marker1")]
			(do
				(doto logger
					(.error marker "Hello Marker World")
					(.warn marker "Hello Marker World")
					(.info marker "Hello Marker World")
					(.debug marker "Hello Marker World")
					(.trace marker "Hello Marker World"))
				(count @log-entries)))
		=> 35))