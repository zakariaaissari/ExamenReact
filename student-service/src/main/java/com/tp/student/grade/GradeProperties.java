package com.tp.student.grade;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Grade thresholds, configurable via application.yml under the "grade" prefix.
 * Sensible French defaults are applied so nothing must be set for it to work.
 *
 * grade:
 *   pass-mark: 10.0
 *   mention:
 *     passable: 10.0
 *     assez-bien: 12.0
 *     bien: 14.0
 *     tres-bien: 16.0
 */
@Component
@ConfigurationProperties(prefix = "grade")
public class GradeProperties {

    /** Minimum moyenne to be "Admis". */
    private double passMark = 10.0;

    private Mention mention = new Mention();

    public double getPassMark() { return passMark; }
    public void setPassMark(double passMark) { this.passMark = passMark; }

    public Mention getMention() { return mention; }
    public void setMention(Mention mention) { this.mention = mention; }

    /** Lower bounds (inclusive) for each mention tier. */
    public static class Mention {
        private double passable = 10.0;
        private double assezBien = 12.0;
        private double bien = 14.0;
        private double tresBien = 16.0;

        public double getPassable() { return passable; }
        public void setPassable(double passable) { this.passable = passable; }

        public double getAssezBien() { return assezBien; }
        public void setAssezBien(double assezBien) { this.assezBien = assezBien; }

        public double getBien() { return bien; }
        public void setBien(double bien) { this.bien = bien; }

        public double getTresBien() { return tresBien; }
        public void setTresBien(double tresBien) { this.tresBien = tresBien; }
    }
}
