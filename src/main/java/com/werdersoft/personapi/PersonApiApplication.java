package com.werdersoft.personapi;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class PersonApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(PersonApiApplication.class, args);
    }

}
