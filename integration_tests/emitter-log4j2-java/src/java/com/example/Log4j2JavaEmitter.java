package com.example;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class Log4j2JavaEmitter {
    final static Logger logger = LogManager.getLogger();

    public static void emit() {
        logger.error("Hello from Log4j 2 (Java)");
        logger.warn("Hello from Log4j 2 (Java)");
        logger.info("Hello from Log4j 2 (Java)");
        logger.debug("Hello from Log4j 2 (Java)");
        logger.trace("Hello from Log4j 2 (Java)");
    }
}
