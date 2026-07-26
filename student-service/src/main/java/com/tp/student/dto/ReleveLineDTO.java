package com.tp.student.dto;

/**
 * One row of the relevé notes table: subject, grade /20, coefficient, and the
 * per-line mention. `passed` (grade >= 10) drives the green/red colouring.
 */
public class ReleveLineDTO {

    private String subject;
    private Double value;
    private Double coefficient;
    private String mention;
    private boolean passed;

    public ReleveLineDTO() {}

    public ReleveLineDTO(String subject, Double value, Double coefficient, String mention, boolean passed) {
        this.subject = subject;
        this.value = value;
        this.coefficient = coefficient;
        this.mention = mention;
        this.passed = passed;
    }

    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }

    public Double getValue() { return value; }
    public void setValue(Double value) { this.value = value; }

    public Double getCoefficient() { return coefficient; }
    public void setCoefficient(Double coefficient) { this.coefficient = coefficient; }

    public String getMention() { return mention; }
    public void setMention(String mention) { this.mention = mention; }

    public boolean isPassed() { return passed; }
    public void setPassed(boolean passed) { this.passed = passed; }
}
