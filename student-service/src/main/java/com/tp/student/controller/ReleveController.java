package com.tp.student.controller;

import com.tp.student.dto.ReleveDTO;
import com.tp.student.releve.RelevePdfService;
import com.tp.student.releve.ReleveService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.Normalizer;

/**
 * Relevé (transcript) endpoints. Kept separate from StudentController and under
 * /api/students so the existing /students CRUD is completely untouched.
 */
@RestController
@RequestMapping("/api/students")
public class ReleveController {

    private final ReleveService releveService;
    private final RelevePdfService pdfService;

    public ReleveController(ReleveService releveService, RelevePdfService pdfService) {
        this.releveService = releveService;
        this.pdfService = pdfService;
    }

    // GET /api/students/{id}/releve  → JSON transcript with computed fields
    @GetMapping("/{id}/releve")
    public ResponseEntity<ReleveDTO> getReleve(@PathVariable Long id) {
        return ResponseEntity.ok(releveService.buildReleve(id));
    }

    // GET /api/students/{id}/releve/pdf  → downloadable styled PDF
    @GetMapping("/{id}/releve/pdf")
    public ResponseEntity<byte[]> getRelevePdf(@PathVariable Long id) {
        ReleveDTO releve = releveService.buildReleve(id);
        byte[] pdf = pdfService.generatePdf(releve);

        String filename = "releve_" + slug(releve.getLastName()) + "_" + releve.getAcademicYear() + ".pdf";

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .body(pdf);
    }

    /** Filename-safe token: strip accents, keep alphanumerics, collapse to underscores. */
    private String slug(String value) {
        if (value == null || value.isBlank()) {
            return "etudiant";
        }
        String noAccents = Normalizer.normalize(value, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "");
        String cleaned = noAccents.replaceAll("[^A-Za-z0-9]+", "_")
                .replaceAll("^_+|_+$", "");
        return cleaned.isBlank() ? "etudiant" : cleaned.toLowerCase();
    }
}
