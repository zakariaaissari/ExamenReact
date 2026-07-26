package com.tp.note.dto;

/**
 * Mirrors the response returned by student-service GET /students/{id}.
 * Feign deserializes the JSON from student-service into this object.
 */
public class StudentResponse {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;

    public StudentResponse() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}
