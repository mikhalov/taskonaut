package com.mikhalov.taskonaut.service;

import com.mikhalov.taskonaut.dto.NoteDTO;
import com.mikhalov.taskonaut.mapper.NoteMapper;
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
    private final NoteMapper noteMapper;

    public void createNote(NoteDTO noteDTO) {
        Note note = noteMapper.toNote(noteDTO);
        noteRepository.save(note);
    }

    public void updateNote(String id, NoteDTO noteDTO) {
        Optional<Note> existingNote = noteRepository.findById(id);
        Note note = existingNote.orElseThrow(() -> new EntityNotFoundException("Note not found with id: " + id));
        noteMapper.updateNote(noteDTO, note);
        note.setLastModifiedDate(LocalDateTime.now());

        noteRepository.save(note);
    }

    public void deleteNote(String id) {
        noteRepository.deleteById(id);
    }

    public NoteDTO getNoteById(String id) {
        Note note = noteRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Note not found with id: " + id));
        return noteMapper.toNoteData(note);
    }

    public List<NoteDTO> getAllNotes() {
        List<Note> notes = noteRepository.findAllByOrderByLastModifiedDateDesc();
        return noteMapper.toNoteDataList(notes);
    }
}

