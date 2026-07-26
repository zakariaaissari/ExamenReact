package com.tp.note.client;

import com.tp.note.dto.StudentResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Feign client for student-service.
 * name = "student-service" must match spring.application.name in student-service.
 */
@FeignClient(name = "student-service")
public interface StudentClient {

    @GetMapping("/students/{id}")
    StudentResponse getStudentById(@PathVariable("id") Long id);
}
