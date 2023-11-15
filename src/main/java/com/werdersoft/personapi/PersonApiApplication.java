package com.werdersoft.personapi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PersonApiApplication {

    private static final Logger LOGGER = LoggerFactory.getLogger(PersonApiApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(PersonApiApplication.class, args);
        LOGGER.info("App is running");
    }

}
