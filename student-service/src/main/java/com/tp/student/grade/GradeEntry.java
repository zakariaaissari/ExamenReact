package com.tp.student.grade;

/**
 * One graded item feeding the moyenne computation. Decoupled from any Feign DTO
 * so the calculator stays pure and unit-testable. A null coefficient is
 * normalised to 1 (backward compatible with notes created before coefficients).
 */
public record GradeEntry(double value, double coefficient) {

    public static GradeEntry of(double value, Double coefficient) {
        return new GradeEntry(value, coefficient != null ? coefficient : 1.0);
    }
}
