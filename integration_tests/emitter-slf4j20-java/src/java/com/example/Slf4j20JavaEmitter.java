package com.example;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.lang.invoke.MethodHandles;

public class Slf4j20JavaEmitter {
    final static Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public static void emit() {
        logger.error("Hello from SLF4J 2.0 (Java)");
        logger.warn("Hello from SLF4J 2.0 (Java)");
        logger.info("Hello from SLF4J 2.0 (Java)");
        logger.debug("Hello from SLF4J 2.0 (Java)");
        logger.trace("Hello from SLF4J 2.0 (Java)");
    }
}
