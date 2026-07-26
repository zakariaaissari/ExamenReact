package com.tp.student.dto;

/**
 * Output DTO returned to the client.
 * Combines local student data with program details fetched from program-service.
 * program may be null if program-service is unavailable or programId is not set.
 */
public class StudentResponse {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private Long programId;
    private ProgramResponse program;   // enriched from program-service via Feign

    public StudentResponse() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public Long getProgramId() { return programId; }
    public void setProgramId(Long programId) { this.programId = programId; }

    public ProgramResponse getProgram() { return program; }
    public void setProgram(ProgramResponse program) { this.program = program; }
}
