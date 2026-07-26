package com.tp.note.service;

import com.tp.note.client.StudentClient;
import com.tp.note.dto.NoteRequest;
import com.tp.note.dto.NoteResponse;
import com.tp.note.dto.StudentResponse;
import com.tp.note.entity.Note;
import com.tp.note.exception.NoteNotFoundException;
import com.tp.note.repository.NoteRepository;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class NoteServiceImpl implements NoteService {

    private static final Logger log = LoggerFactory.getLogger(NoteServiceImpl.class);

    private final NoteRepository noteRepository;
    private final StudentClient studentClient;

    public NoteServiceImpl(NoteRepository noteRepository, StudentClient studentClient) {
        this.noteRepository = noteRepository;
        this.studentClient = studentClient;
    }

    @Override
    public List<NoteResponse> getAllNotes() {
        return noteRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public NoteResponse getNoteById(Long id) {
        Note note = noteRepository.findById(id)
                .orElseThrow(() -> new NoteNotFoundException(id));
        return toResponse(note);
    }

    @Override
    public List<NoteResponse> getNotesByStudentId(Long studentId) {
        return noteRepository.findByStudentId(studentId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public NoteResponse createNote(NoteRequest request) {
        // Validate that the student exists before saving the note
        try {
            studentClient.getStudentById(request.getStudentId());
        } catch (FeignException.NotFound e) {
            throw new IllegalArgumentException("Student not found with id: " + request.getStudentId());
        } catch (FeignException e) {
            log.warn("Could not validate student {} — proceeding anyway: {}", request.getStudentId(), e.getMessage());
        }

        Note note = new Note(
                request.getStudentId(),
                request.getSubject(),
                request.getValue(),
                request.getComment()
        );
        note.setCoefficient(resolveCoefficient(request.getCoefficient()));
        return toResponse(noteRepository.save(note));
    }

    @Override
    public NoteResponse updateNote(Long id, NoteRequest request) {
        Note note = noteRepository.findById(id)
                .orElseThrow(() -> new NoteNotFoundException(id));

        note.setStudentId(request.getStudentId());
        note.setSubject(request.getSubject());
        note.setValue(request.getValue());
        note.setCoefficient(resolveCoefficient(request.getCoefficient()));
        note.setComment(request.getComment());

        return toResponse(noteRepository.save(note));
    }

    @Override
    public void deleteNote(Long id) {
        if (!noteRepository.existsById(id)) {
            throw new NoteNotFoundException(id);
        }
        noteRepository.deleteById(id);
    }

    // A missing coefficient means "weight 1" — keeps pre-existing notes valid.
    private Double resolveCoefficient(Double coefficient) {
        return coefficient != null ? coefficient : 1.0;
    }

    private NoteResponse toResponse(Note note) {
        NoteResponse response = new NoteResponse();
        response.setId(note.getId());
        response.setSubject(note.getSubject());
        response.setValue(note.getValue());
        response.setCoefficient(resolveCoefficient(note.getCoefficient()));
        response.setComment(note.getComment());
        response.setStudentId(note.getStudentId());

        try {
            StudentResponse student = studentClient.getStudentById(note.getStudentId());
            response.setStudent(student);
        } catch (FeignException e) {
            log.warn("Could not fetch student {} for note {}: {}", note.getStudentId(), note.getId(), e.getMessage());
        }

        return response;
    }
}
