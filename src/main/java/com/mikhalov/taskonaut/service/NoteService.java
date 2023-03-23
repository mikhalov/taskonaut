package com.mikhalov.taskonaut.service;

import com.mikhalov.taskonaut.model.Note;
import com.mikhalov.taskonaut.repository.NoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NoteService {

    private final NoteRepository noteRepository;

    public Note createNote(Note note) {
        return noteRepository.save(note);
    }

    public Note updateNote(String id, Note note) {
        Optional<Note> existingNote = noteRepository.findById(id);
        Note updatedNote = existingNote.orElseThrow(() -> new EntityNotFoundException("Note not found with id: " + id));
        updatedNote.setTitle(note.getTitle());
        updatedNote.setContent(note.getContent());
        updatedNote.setLastModifiedDate(LocalDateTime.now());

        return noteRepository.save(updatedNote);
    }

    public void deleteNote(String id) {
        noteRepository.deleteById(id);
    }

    public Optional<Note> getNoteById(String id) {
        return noteRepository.findById(id);
    }

    public List<Note> getAllNotes() {
        return noteRepository.findAll();
    }
}

