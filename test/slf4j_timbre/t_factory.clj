(ns slf4j-timbre.t-factory
	(:use midje.sweet))

(let
	[logger-1a (org.slf4j.LoggerFactory/getLogger "logger-1")
	 logger-2  (org.slf4j.LoggerFactory/getLogger "logger-2")
	 logger-1b (org.slf4j.LoggerFactory/getLogger "logger-1")
	 logger-1c (future (org.slf4j.LoggerFactory/getLogger "logger-1"))]

	(fact "factory returns same instances for same names"
		(= logger-1a logger-1b) => true)

	(fact "factory returns different instances for different names"
		(= logger-1a logger-2) => false)

	(fact "factory returns same instances for same names across different threads"
		(= logger-1a @logger-1c) => true))