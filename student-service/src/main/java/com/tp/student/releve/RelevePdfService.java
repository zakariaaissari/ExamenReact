package com.tp.student.releve;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import com.tp.student.dto.ReleveDTO;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * Renders the "releve" Thymeleaf template to XHTML and converts it to a PDF
 * with openhtmltopdf (PDFBox). All styling is inline in the template, so no
 * external resources need resolving.
 */
@Service
public class RelevePdfService {

    private static final DateTimeFormatter DATE_FR =
            DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.FRENCH);

    private final SpringTemplateEngine templateEngine;

    public RelevePdfService(SpringTemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    public byte[] generatePdf(ReleveDTO releve) {
        Context context = new Context(Locale.FRENCH);
        context.setVariable("r", releve);
        context.setVariable("dateStr", releve.getDate() != null ? releve.getDate().format(DATE_FR) : "");

        String html = templateEngine.process("releve", context);

        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.useFastMode();
            builder.withHtmlContent(html, null);
            builder.toStream(out);
            builder.run();
            return out.toByteArray();
        } catch (Exception e) {
            throw new IllegalStateException("Failed to generate relevé PDF", e);
        }
    }
}
