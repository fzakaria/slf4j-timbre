package com.github.fzakaria.slf4j.timbre;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TimbreLoggerAdapterTest {

    @Test
    public void basicLogging() {
        Logger log = LoggerFactory.getLogger("basicLoggin");
        log.info("Hello World");
        log.info("Hello World {}", "Farid");
        log.info("Hello World {} {}", "Farid", "Zakaria");
        log.info("Hello World {} {} {}", "What", "a", "Beautiful Day!");
        log.info("Hello World", new Exception("test"));
        log.info("Hello World {} {}", "Farid", "Zakaria", new Exception("test"));
    }
}
