package com.tp.note.dto;

import jakarta.validation.constraints.*;

public class NoteRequest {

    @NotNull(message = "Student ID is required")
    private Long studentId;

    @NotBlank(message = "Subject is required")
    private String subject;

    @NotNull(message = "Value is required")
    @Min(value = 0, message = "Value must be at least 0")
    @Max(value = 20, message = "Value must be at most 20")
    private Double value;

    // Optional: defaults to 1 in the service when omitted, so existing clients
    // that don't send it keep working unchanged.
    @DecimalMin(value = "0", message = "Coefficient must be at least 0")
    private Double coefficient;

    private String comment;

    public NoteRequest() {}

    public Long getStudentId() { return studentId; }
    public void setStudentId(Long studentId) { this.studentId = studentId; }

    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }

    public Double getValue() { return value; }
    public void setValue(Double value) { this.value = value; }

    public Double getCoefficient() { return coefficient; }
    public void setCoefficient(Double coefficient) { this.coefficient = coefficient; }

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }
}
