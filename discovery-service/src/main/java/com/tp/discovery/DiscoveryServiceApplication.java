package com.tp.discovery;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * Eureka Server — the service registry for the entire microservices system.
 *
 * @EnableEurekaServer activates the embedded Eureka server. Every other
 * microservice will register its name and address here on startup, and
 * look up peer service addresses at runtime.
 */
@SpringBootApplication
@EnableEurekaServer
public class DiscoveryServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(DiscoveryServiceApplication.class, args);
    }
}
