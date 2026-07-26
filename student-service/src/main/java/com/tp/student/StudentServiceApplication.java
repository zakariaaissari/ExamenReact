package com.tp.student;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * Student microservice entry point.
 *
 * @EnableDiscoveryClient  — registers with Eureka on startup.
 * @EnableFeignClients     — scans for @FeignClient interfaces and creates
 *                           proxy beans that call remote services by name.
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class StudentServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(StudentServiceApplication.class, args);
    }
}
