package com.tp.student.dto;

import java.time.LocalDate;
import java.util.List;

/**
 * The full relevé de notes (transcript): student + program identity, the graded
 * lines, and the computed summary (moyenne, mention, décision, rang). Serialised
 * to JSON for the preview endpoint and fed to the Thymeleaf template for the PDF.
 */
public class ReleveDTO {

    // Student identity
    private Long studentId;
    private String firstName;
    private String lastName;
    private String fullName;
    private String email;

    // Program / filière (nested, reuses the existing ProgramResponse)
    private ProgramResponse program;

    // Institution context (from configuration)
    private String institution;
    private String academicYear;
    private String lieu;
    private LocalDate date;

    // Notes + computed results
    private List<ReleveLineDTO> notes;
    private double moyenne;
    private String mention;
    private String decision;
    private int rang;
    private int effectif;
    private String rangLabel;   // e.g. "3 / 25"
    private boolean admis;

    public ReleveDTO() {}

    public Long getStudentId() { return studentId; }
    public void setStudentId(Long studentId) { this.studentId = studentId; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public ProgramResponse getProgram() { return program; }
    public void setProgram(ProgramResponse program) { this.program = program; }

    public String getInstitution() { return institution; }
    public void setInstitution(String institution) { this.institution = institution; }

    public String getAcademicYear() { return academicYear; }
    public void setAcademicYear(String academicYear) { this.academicYear = academicYear; }

    public String getLieu() { return lieu; }
    public void setLieu(String lieu) { this.lieu = lieu; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public List<ReleveLineDTO> getNotes() { return notes; }
    public void setNotes(List<ReleveLineDTO> notes) { this.notes = notes; }

    public double getMoyenne() { return moyenne; }
    public void setMoyenne(double moyenne) { this.moyenne = moyenne; }

    public String getMention() { return mention; }
    public void setMention(String mention) { this.mention = mention; }

    public String getDecision() { return decision; }
    public void setDecision(String decision) { this.decision = decision; }

    public int getRang() { return rang; }
    public void setRang(int rang) { this.rang = rang; }

    public int getEffectif() { return effectif; }
    public void setEffectif(int effectif) { this.effectif = effectif; }

    public String getRangLabel() { return rangLabel; }
    public void setRangLabel(String rangLabel) { this.rangLabel = rangLabel; }

    public boolean isAdmis() { return admis; }
    public void setAdmis(boolean admis) { this.admis = admis; }
}
