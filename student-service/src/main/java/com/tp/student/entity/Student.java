package com.tp.student.entity;

import jakarta.persistence.*;

/**
 * Owns only the data that belongs to this service.
 * programId is a plain Long — NOT a JPA @ManyToOne — because program-service
 * owns that data. Cross-service joins are done at the service layer via Feign.
 */
@Entity
@Table(name = "students")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(name = "program_id")
    private Long programId;

    public Student() {}

    public Student(String firstName, String lastName, String email, Long programId) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.programId = programId;
    }

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
}
