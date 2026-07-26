package com.tp.student.grade;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Unit tests for the pure grade computation. Uses default thresholds
 * (GradeProperties with no overrides) so it needs no Spring context.
 */
class GradeCalculatorTest {

    private final GradeCalculator calc = new GradeCalculator(new GradeProperties());

    @Test
    void moyenne_withEqualCoefficients_equalsSimpleAverage() {
        List<GradeEntry> notes = List.of(
                new GradeEntry(10.0, 1.0),
                new GradeEntry(14.0, 1.0)
        );
        assertEquals(12.0, calc.computeMoyenne(notes));
    }

    @Test
    void moyenne_isWeightedByCoefficient() {
        // (16*3 + 8*1) / (3+1) = 56/4 = 14
        List<GradeEntry> notes = List.of(
                new GradeEntry(16.0, 3.0),
                new GradeEntry(8.0, 1.0)
        );
        assertEquals(14.0, calc.computeMoyenne(notes));
    }

    @Test
    void moyenne_nullCoefficient_treatedAsOne() {
        // GradeEntry.of normalises a null coefficient to 1 → simple average
        List<GradeEntry> notes = List.of(
                GradeEntry.of(12.0, null),
                GradeEntry.of(18.0, null)
        );
        assertEquals(15.0, calc.computeMoyenne(notes));
    }

    @Test
    void moyenne_isRoundedToTwoDecimals() {
        // (13 + 14 + 16) / 3 = 14.333... -> 14.33
        List<GradeEntry> notes = List.of(
                new GradeEntry(13.0, 1.0),
                new GradeEntry(14.0, 1.0),
                new GradeEntry(16.0, 1.0)
        );
        assertEquals(14.33, calc.computeMoyenne(notes));
    }

    @Test
    void moyenne_emptyList_isZero() {
        assertEquals(0.0, calc.computeMoyenne(List.of()));
    }

    @Test
    void mention_coversAllTiersAtBoundaries() {
        assertEquals("Insuffisant", calc.computeMention(9.99));
        assertEquals("Passable", calc.computeMention(10.0));
        assertEquals("Passable", calc.computeMention(11.99));
        assertEquals("Assez Bien", calc.computeMention(12.0));
        assertEquals("Bien", calc.computeMention(14.0));
        assertEquals("Très Bien", calc.computeMention(16.0));
        assertEquals("Très Bien", calc.computeMention(20.0));
    }

    @Test
    void decision_admisAtPassMark_ajourneBelow() {
        assertEquals("Admis", calc.computeDecision(10.0));
        assertEquals("Admis", calc.computeDecision(15.5));
        assertEquals("Ajourné", calc.computeDecision(9.99));
        assertEquals("Ajourné", calc.computeDecision(0.0));
    }

    @Test
    void rang_countsStrictlyHigherPlusOne() {
        List<Double> program = List.of(17.0, 15.0, 12.0, 9.0);
        assertEquals(1, calc.computeRang(17.0, program)); // top
        assertEquals(3, calc.computeRang(12.0, program)); // two above
        assertEquals(5, calc.computeRang(8.0, program));  // below everyone
    }

    @Test
    void rang_tiesShareSameRank() {
        List<Double> program = List.of(15.0, 15.0, 10.0);
        // no one strictly above 15 -> both are rank 1
        assertEquals(1, calc.computeRang(15.0, program));
        // two strictly above 10 -> rank 3
        assertEquals(3, calc.computeRang(10.0, program));
    }
}
