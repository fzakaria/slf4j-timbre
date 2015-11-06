package com.github.fzakaria.slf4j.timbre;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TimbreLoggerAdapterTest {

    @Test
    public void basicErrorLogging() {
        Logger log = LoggerFactory.getLogger("basicLoggin");
        log.error("Hello World");
        log.error("Hello World {}", "Farid");
        log.error("Hello World {} {}", "Farid", "Zakaria");
        log.error("Hello World {} {} {}", "What", "a", "Beautiful Day!");
        log.error("Hello World", new Exception("test"));
        log.error("Hello World {} {}", "Farid", "Zakaria", new Exception("test"));
    }

    @Test
    public void basicWarnLogging() {
        Logger log = LoggerFactory.getLogger("basicLoggin");
        log.warn("Hello World");
        log.warn("Hello World {}", "Farid");
        log.warn("Hello World {} {}", "Farid", "Zakaria");
        log.warn("Hello World {} {} {}", "What", "a", "Beautiful Day!");
        log.warn("Hello World", new Exception("test"));
        log.warn("Hello World {} {}", "Farid", "Zakaria", new Exception("test"));
    }

    @Test
    public void basicInfoLogging() {
        Logger log = LoggerFactory.getLogger("basicLoggin");
        log.info("Hello World");
        log.info("Hello World {}", "Farid");
        log.info("Hello World {} {}", "Farid", "Zakaria");
        log.info("Hello World {} {} {}", "What", "a", "Beautiful Day!");
        log.info("Hello World", new Exception("test"));
        log.info("Hello World {} {}", "Farid", "Zakaria", new Exception("test"));
    }

    @Test
    public void basicDebugLogging() {
        Logger log = LoggerFactory.getLogger("basicLoggin");
        log.debug("Hello World");
        log.debug("Hello World {}", "Farid");
        log.debug("Hello World {} {}", "Farid", "Zakaria");
        log.debug("Hello World {} {} {}", "What", "a", "Beautiful Day!");
        log.debug("Hello World", new Exception("test"));
        log.debug("Hello World {} {}", "Farid", "Zakaria", new Exception("test"));
    }

    @Test
    public void basicTraceLogging() {
        Logger log = LoggerFactory.getLogger("basicLoggin");
        log.trace("Hello World");
        log.trace("Hello World {}", "Farid");
        log.trace("Hello World {} {}", "Farid", "Zakaria");
        log.trace("Hello World {} {} {}", "What", "a", "Beautiful Day!");
        log.trace("Hello World", new Exception("test"));
        log.trace("Hello World {} {}", "Farid", "Zakaria", new Exception("test"));
    }
    
}
