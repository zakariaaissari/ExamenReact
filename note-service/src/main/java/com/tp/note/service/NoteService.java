package com.tp.note.service;

import com.tp.note.dto.NoteRequest;
import com.tp.note.dto.NoteResponse;

import java.util.List;

public interface NoteService {

    List<NoteResponse> getAllNotes();

    NoteResponse getNoteById(Long id);

    List<NoteResponse> getNotesByStudentId(Long studentId);

    NoteResponse createNote(NoteRequest request);

    NoteResponse updateNote(Long id, NoteRequest request);

    void deleteNote(Long id);
}
