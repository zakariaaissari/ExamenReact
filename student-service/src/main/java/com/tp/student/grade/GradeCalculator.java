package com.tp.student.grade;

import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Pure grade computation layer. No web/persistence concerns — takes plain
 * {@link GradeEntry} values and returns computed results, so it is trivially
 * unit-testable and reusable by the relevé (transcript) feature.
 */
@Service
public class GradeCalculator {

    private final GradeProperties props;

    public GradeCalculator(GradeProperties props) {
        this.props = props;
    }

    /**
     * Weighted average by coefficient, rounded to 2 decimals.
     * With every coefficient = 1 this equals the simple average, so notes
     * without an explicit coefficient behave exactly as before.
     * Returns 0 for an empty list (callers decide how to present "no notes").
     */
    public double computeMoyenne(List<GradeEntry> notes) {
        if (notes == null || notes.isEmpty()) {
            return 0.0;
        }
        double weightedSum = 0.0;
        double coefSum = 0.0;
        for (GradeEntry n : notes) {
            weightedSum += n.value() * n.coefficient();
            coefSum += n.coefficient();
        }
        if (coefSum == 0.0) {
            return 0.0;
        }
        return round2(weightedSum / coefSum);
    }

    /** French mention from the configured thresholds. */
    public String computeMention(double moyenne) {
        GradeProperties.Mention m = props.getMention();
        if (moyenne >= m.getTresBien()) return "Très Bien";
        if (moyenne >= m.getBien()) return "Bien";
        if (moyenne >= m.getAssezBien()) return "Assez Bien";
        if (moyenne >= m.getPassable()) return "Passable";
        return "Insuffisant";
    }

    /** "Admis" if the moyenne reaches the pass mark, otherwise "Ajourné". */
    public String computeDecision(double moyenne) {
        return moyenne >= props.getPassMark() ? "Admis" : "Ajourné";
    }

    /**
     * 1-based rank of a moyenne within a program: number of strictly higher
     * moyennes, plus one. Ties share the same rank (competition ranking).
     */
    public int computeRang(double studentMoyenne, List<Double> programMoyennes) {
        if (programMoyennes == null || programMoyennes.isEmpty()) {
            return 1;
        }
        long higher = programMoyennes.stream().filter(m -> m > studentMoyenne).count();
        return (int) higher + 1;
    }

    private double round2(double v) {
        return Math.round(v * 100.0) / 100.0;
    }
}
