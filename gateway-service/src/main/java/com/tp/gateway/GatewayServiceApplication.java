package com.tp.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * API Gateway — single entry point for all external traffic.
 *
 * @EnableDiscoveryClient registers this service with Eureka and enables
 * the lb:// scheme in routes, which resolves service names to real addresses
 * via the Eureka registry at runtime.
 */
@SpringBootApplication
@EnableDiscoveryClient
public class GatewayServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(GatewayServiceApplication.class, args);
    }
}
