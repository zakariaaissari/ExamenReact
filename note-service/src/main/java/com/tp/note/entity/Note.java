package com.tp.note.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "notes")
public class Note {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "student_id", nullable = false)
    private Long studentId;

    @Column(nullable = false)
    private String subject;

    @Column(nullable = false)
    private Double value;  // grade between 0 and 20

    // Nullable on purpose: existing rows created before this column keep NULL,
    // which the grade computation treats as a coefficient of 1 (backward compatible).
    @Column(nullable = true)
    private Double coefficient = 1.0;

    private String comment;

    public Note() {}

    public Note(Long studentId, String subject, Double value, String comment) {
        this.studentId = studentId;
        this.subject = subject;
        this.value = value;
        this.comment = comment;
    }

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
