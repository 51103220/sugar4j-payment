package com.sacombank.sugar.demo.payment.infrastructure.configuration;

import javax.annotation.PreDestroy;

import org.apache.logging.log4j.LogManager;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class Initializer implements ApplicationRunner {
    private static final org.apache.logging.log4j.Logger logger = LogManager.getLogger(Initializer.class);

    @Override
    public void run(ApplicationArguments args) throws Exception {
        logger.info("initalizing application...");
    }

    @PreDestroy
    public void onExit() {
        logger.info("application stopped");
    }
    
}
