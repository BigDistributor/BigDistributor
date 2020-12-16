package com.bigdistributor.plugin.data;

import java.util.logging.Logger;

public class HelloClass {
    private static final Logger LOGGER = Logger.getLogger(HelloClass.class.getName());

    public void sayHello() {
        LOGGER.info("Hello from class");
    }
}