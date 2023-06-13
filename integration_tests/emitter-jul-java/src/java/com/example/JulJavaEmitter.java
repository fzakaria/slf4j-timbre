package com.example;
import java.util.logging.Logger;
import java.lang.invoke.MethodHandles;

public class JulJavaEmitter {
    final static Logger logger = Logger.getLogger(MethodHandles.lookup().lookupClass().getName());

    public static void emit() {
        logger.severe("Hello from JUL (Java)");
        logger.warning("Hello from JUL (Java)");
        logger.info("Hello from JUL (Java)");
        logger.fine("Hello from JUL (Java)");
        logger.finest("Hello from JUL (Java)");
    }
}
