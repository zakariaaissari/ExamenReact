package com.tp.student.releve;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Institution / academic-year text shown on the relevé, configurable via
 * application.yml under the "releve" prefix. Defaults let it work out of the box.
 *
 * releve:
 *   institution: "Université Hassan II - Faculté des Sciences"
 *   academic-year: "2025-2026"
 *   lieu: "Casablanca"
 */
@Component
@ConfigurationProperties(prefix = "releve")
public class ReleveProperties {

    private String institution = "Université Hassan II - Faculté des Sciences";
    private String academicYear = "2025-2026";
    private String lieu = "Casablanca";

    public String getInstitution() { return institution; }
    public void setInstitution(String institution) { this.institution = institution; }

    public String getAcademicYear() { return academicYear; }
    public void setAcademicYear(String academicYear) { this.academicYear = academicYear; }

    public String getLieu() { return lieu; }
    public void setLieu(String lieu) { this.lieu = lieu; }
}
