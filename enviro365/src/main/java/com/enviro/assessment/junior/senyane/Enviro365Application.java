package com.enviro.assessment.junior.senyane;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Entry point for the Enviro365 Withdrawal Notice System.
 *
 * To run:
 *   mvn spring-boot:run
 *
 * H2 Console: http://localhost:8080/h2-console
 * API Base:   http://localhost:8080/api
 * Frontend:   http://localhost:8080/index.html
 */
@SpringBootApplication
public class Enviro365Application {

    public static void main(String[] args) {
        SpringApplication.run(Enviro365Application.class, args);
    }
}
