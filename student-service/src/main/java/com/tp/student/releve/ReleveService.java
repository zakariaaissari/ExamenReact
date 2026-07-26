package com.tp.student.releve;

import com.tp.student.client.NoteClient;
import com.tp.student.client.ProgramClient;
import com.tp.student.dto.NoteResponse;
import com.tp.student.dto.ProgramResponse;
import com.tp.student.dto.ReleveDTO;
import com.tp.student.dto.ReleveLineDTO;
import com.tp.student.entity.Student;
import com.tp.student.exception.StudentNotFoundException;
import com.tp.student.grade.GradeCalculator;
import com.tp.student.grade.GradeEntry;
import com.tp.student.repository.StudentRepository;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

/**
 * Builds the relevé for a student by aggregating Student + Program (Feign) +
 * Notes (Feign) and delegating all arithmetic to {@link GradeCalculator}.
 */
@Service
public class ReleveService {

    private static final Logger log = LoggerFactory.getLogger(ReleveService.class);
    private static final double PASS_LINE = 10.0; // colour threshold per line

    private final StudentRepository studentRepository;
    private final ProgramClient programClient;
    private final NoteClient noteClient;
    private final GradeCalculator calculator;
    private final ReleveProperties props;

    public ReleveService(StudentRepository studentRepository,
                         ProgramClient programClient,
                         NoteClient noteClient,
                         GradeCalculator calculator,
                         ReleveProperties props) {
        this.studentRepository = studentRepository;
        this.programClient = programClient;
        this.noteClient = noteClient;
        this.calculator = calculator;
        this.props = props;
    }

    public ReleveDTO buildReleve(Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new StudentNotFoundException(studentId));

        List<NoteResponse> notes = fetchNotes(studentId);
        if (notes.isEmpty()) {
            // Clean 404 via the existing global handler.
            throw new StudentNotFoundException(
                    "No notes found for student id: " + studentId + " — relevé unavailable");
        }

        // Compute the summary from this student's notes.
        List<GradeEntry> entries = notes.stream()
                .map(n -> GradeEntry.of(n.getValue(), n.getCoefficient()))
                .toList();
        double moyenne = calculator.computeMoyenne(entries);
        String mention = calculator.computeMention(moyenne);
        String decision = calculator.computeDecision(moyenne);

        // Rank within the program.
        int rang = 1;
        int effectif = 1;
        if (student.getProgramId() != null) {
            List<Student> classmates = studentRepository.findByProgramId(student.getProgramId());
            List<Double> moyennes = classmates.stream()
                    .map(s -> calculator.computeMoyenne(
                            fetchNotes(s.getId()).stream()
                                    .map(n -> GradeEntry.of(n.getValue(), n.getCoefficient()))
                                    .toList()))
                    .toList();
            rang = calculator.computeRang(moyenne, moyennes);
            effectif = classmates.size();
        }

        return assemble(student, notes, moyenne, mention, decision, rang, effectif);
    }

    private ReleveDTO assemble(Student student, List<NoteResponse> notes,
                               double moyenne, String mention, String decision,
                               int rang, int effectif) {
        ReleveDTO dto = new ReleveDTO();
        dto.setStudentId(student.getId());
        dto.setFirstName(student.getFirstName());
        dto.setLastName(student.getLastName());
        dto.setFullName(student.getFirstName() + " " + student.getLastName());
        dto.setEmail(student.getEmail());
        dto.setProgram(fetchProgram(student.getProgramId()));

        dto.setInstitution(props.getInstitution());
        dto.setAcademicYear(props.getAcademicYear());
        dto.setLieu(props.getLieu());
        dto.setDate(LocalDate.now());

        dto.setNotes(notes.stream().map(this::toLine).toList());
        dto.setMoyenne(moyenne);
        dto.setMention(mention);
        dto.setDecision(decision);
        dto.setRang(rang);
        dto.setEffectif(effectif);
        dto.setRangLabel(rang + " / " + effectif);
        dto.setAdmis("Admis".equals(decision));
        return dto;
    }

    private ReleveLineDTO toLine(NoteResponse n) {
        double coef = n.getCoefficient() != null ? n.getCoefficient() : 1.0;
        boolean passed = n.getValue() != null && n.getValue() >= PASS_LINE;
        String lineMention = n.getValue() != null ? calculator.computeMention(n.getValue()) : "-";
        return new ReleveLineDTO(n.getSubject(), n.getValue(), coef, lineMention, passed);
    }

    private List<NoteResponse> fetchNotes(Long studentId) {
        try {
            List<NoteResponse> notes = noteClient.getNotesByStudentId(studentId);
            return notes != null ? notes : Collections.emptyList();
        } catch (FeignException e) {
            log.warn("Could not fetch notes for student {}: {}", studentId, e.getMessage());
            return Collections.emptyList();
        }
    }

    private ProgramResponse fetchProgram(Long programId) {
        if (programId == null) {
            return null;
        }
        try {
            return programClient.getProgramById(programId);
        } catch (FeignException e) {
            log.warn("Could not fetch program {}: {}", programId, e.getMessage());
            return null;
        }
    }
}
