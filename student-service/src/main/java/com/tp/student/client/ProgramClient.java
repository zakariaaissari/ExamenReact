package com.tp.student.client;

import com.tp.student.dto.ProgramResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Feign client for program-service.
 *
 * name = "program-service" must exactly match the spring.application.name
 * declared in program-service's application.yml. Feign + Eureka resolve
 * this name to a real host:port at call time — no URL is hardcoded.
 *
 * At runtime Spring creates a proxy of this interface. Calling
 * getProgramById(1L) triggers an actual HTTP GET to program-service.
 */
@FeignClient(name = "program-service")
public interface ProgramClient {

    @GetMapping("/programs/{id}")
    ProgramResponse getProgramById(@PathVariable("id") Long id);
}
