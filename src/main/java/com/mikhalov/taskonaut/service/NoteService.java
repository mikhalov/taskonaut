package com.mikhalov.taskonaut.service;

import com.mikhalov.taskonaut.model.Note;
import com.mikhalov.taskonaut.repository.NoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NoteService {

    private final NoteRepository noteRepository;

    public void createNote(Note note) {
        noteRepository.save(note);
    }

    public void updateNote(Note note) {
        noteRepository.save(note);
    }

    public void deleteNote(String id) {
        noteRepository.deleteById(id);
    }

    public Note getNoteById(String id) {
        return noteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Note not found with id: " + id));
    }

    public List<Note> getAllNotes() {
        return noteRepository.findAllByOrderByLastModifiedDateDesc();
    }

    public List<Note> getAllByLabelName(String labelName) {
        return noteRepository.findByLabelNameOrderByLastModifiedDateDesc(labelName);
    }

}

