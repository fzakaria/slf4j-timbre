package com.example;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import java.lang.invoke.MethodHandles;

public class JclJavaEmitter {
    final static Log logger = LogFactory.getLog(MethodHandles.lookup().lookupClass());

    public static void emit() {
        logger.error("Hello from JCL (Java)");
        logger.warn("Hello from JCL (Java)");
        logger.info("Hello from JCL (Java)");
        logger.debug("Hello from JCL (Java)");
        logger.trace("Hello from JCL (Java)");
    }
}
