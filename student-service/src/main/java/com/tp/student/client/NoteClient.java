package com.tp.student.client;

import com.tp.student.dto.NoteResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * Feign client for note-service, used by the relevé (transcript) feature to
 * pull a student's notes. name = "note-service" matches its
 * spring.application.name; Eureka resolves it at call time.
 */
@FeignClient(name = "note-service")
public interface NoteClient {

    @GetMapping("/notes/student/{studentId}")
    List<NoteResponse> getNotesByStudentId(@PathVariable("studentId") Long studentId);
}
