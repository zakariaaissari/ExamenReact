package com.tp.student.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Input DTO for POST /students and PUT /students/{id}.
 * Validation annotations are enforced by @Valid in the controller.
 */
public class StudentRequest {

    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    private String email;

    @NotNull(message = "Program ID is required")
    private Long programId;

    public StudentRequest() {}

    public StudentRequest(String firstName, String lastName, String email, Long programId) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.programId = programId;
    }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public Long getProgramId() { return programId; }
    public void setProgramId(Long programId) { this.programId = programId; }
}
