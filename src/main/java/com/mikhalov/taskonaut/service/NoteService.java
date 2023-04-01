package com.mikhalov.taskonaut.service;

import com.mikhalov.taskonaut.dto.NoteDTO;
import com.mikhalov.taskonaut.mapper.NoteMapper;
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
    private final NoteMapper noteMapper;

    public void createNote(NoteDTO noteDTO) {
        noteRepository.save(noteMapper.toNote(noteDTO));
    }


    public void updateNote(NoteDTO noteDTO) {
        Note note = getNoteById(noteDTO.getId());
        noteMapper.updateNote(noteDTO, note);
        updateNote(note);
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

    public NoteDTO getNoteDTOById(String id) {
        return noteMapper.toNoteDTO(getNoteById(id));
    }

    public List<NoteDTO> getAllNotes() {
        return noteRepository.findAllByOrderByLastModifiedDateDesc()
                .stream()
                .map(noteMapper::toNoteDTO)
                .toList();
    }

    public List<NoteDTO> getAllByLabelName(String labelName) {
        return noteRepository.findByLabelNameOrderByLastModifiedDateDesc(labelName)
                .stream()
                .map(noteMapper::toNoteDTO)
                .toList();
    }

}

