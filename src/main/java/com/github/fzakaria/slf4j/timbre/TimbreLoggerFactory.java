package com.github.fzakaria.slf4j.timbre;

import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * TimbreLoggerFactory is an implementation of {@link ILoggerFactory} returning the
 * appropriately named {@link TimbreLoggerFactory} instance.
 *
 */
public class TimbreLoggerFactory implements ILoggerFactory {

    private final ConcurrentMap<String, Logger> loggerMap;

    public TimbreLoggerFactory() {
        loggerMap = new ConcurrentHashMap<String, Logger>();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.slf4j.ILoggerFactory#getLogger(java.lang.String)
     */
    public Logger getLogger(String name) {
        Logger slf4jLogger = loggerMap.get(name);
        if (slf4jLogger != null) {
            return slf4jLogger;
        } else {
            Logger newInstance = new TimbreLoggerAdapter();
            Logger oldInstance = loggerMap.putIfAbsent(name, newInstance);
            return oldInstance == null ? newInstance : oldInstance;
        }
    }
}