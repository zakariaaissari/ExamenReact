package com.tp.student.dto;

/**
 * A note as seen by student-service (fetched from note-service via Feign).
 * Only the fields needed for the relevé are declared; note-service may return
 * more (e.g. a nested student) — Jackson ignores unknown properties by default.
 */
public class NoteResponse {

    private Long id;
    private Long studentId;
    private String subject;
    private Double value;
    private Double coefficient;
    private String comment;

    public NoteResponse() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

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
