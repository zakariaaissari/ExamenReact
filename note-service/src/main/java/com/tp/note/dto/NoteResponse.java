package com.tp.note.dto;

/**
 * Output DTO — combines note data with student details fetched via Feign.
 */
public class NoteResponse {

    private Long id;
    private String subject;
    private Double value;
    private Double coefficient;
    private String comment;
    private Long studentId;
    private StudentResponse student;  // enriched from student-service via Feign

    public NoteResponse() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }

    public Double getValue() { return value; }
    public void setValue(Double value) { this.value = value; }

    public Double getCoefficient() { return coefficient; }
    public void setCoefficient(Double coefficient) { this.coefficient = coefficient; }

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }

    public Long getStudentId() { return studentId; }
    public void setStudentId(Long studentId) { this.studentId = studentId; }

    public StudentResponse getStudent() { return student; }
    public void setStudent(StudentResponse student) { this.student = student; }
}
