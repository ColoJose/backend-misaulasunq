package com.misaulasunq.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggerHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger("General Logger");

    public static void logInfo(String message, Object anObject){
        LOGGER.info(message, anObject);
    }

    public static void logInfo(String message) {
        LOGGER.info(message);
    }
}
