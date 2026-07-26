package com.tp.program;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * Program microservice entry point.
 * Registers with Eureka so gateway and student-service can resolve its address.
 */
@SpringBootApplication
@EnableDiscoveryClient
public class ProgramServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProgramServiceApplication.class, args);
    }
}
