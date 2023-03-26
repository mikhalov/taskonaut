package com.mikhalov.taskonaut.service;

import com.mikhalov.taskonaut.bean.NoteData;
import com.mikhalov.taskonaut.mapper.NoteMapper;
import com.mikhalov.taskonaut.model.Note;
import com.mikhalov.taskonaut.repository.NoteRepository;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NoteService {

    private final NoteRepository noteRepository;
    private final NoteMapper noteMapper = Mappers.getMapper(NoteMapper.class);

    public void createNote(NoteData noteData) {
        Note note = noteMapper.toNote(noteData);
        noteRepository.save(note);
    }

    public void updateNote(String id, NoteData noteData) {
        Optional<Note> existingNote = noteRepository.findById(id);
        Note note = existingNote.orElseThrow(() -> new EntityNotFoundException("Note not found with id: " + id));
        noteMapper.updateNote(noteData, note);
        note.setLastModifiedDate(LocalDateTime.now());

        noteRepository.save(note);
    }

    public void deleteNote(String id) {
        noteRepository.deleteById(id);
    }

    public NoteData getNoteById(String id) {
        Note note = noteRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Note not found with id: " + id));
        return noteMapper.toNoteData(note);
    }

    public List<NoteData> getAllNotes() {
        List<Note> notes = noteRepository.findAllByOrderByLastModifiedDateDesc();
        return noteMapper.toNoteDataList(notes);
    }
}

