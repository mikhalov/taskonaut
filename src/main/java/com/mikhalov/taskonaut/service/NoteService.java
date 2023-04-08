package com.mikhalov.taskonaut.service;

import com.mikhalov.taskonaut.dto.NoteDTO;
import com.mikhalov.taskonaut.mapper.NoteMapper;
import com.mikhalov.taskonaut.model.Note;
import com.mikhalov.taskonaut.model.User;
import com.mikhalov.taskonaut.repository.NoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NoteService {

    private final NoteRepository noteRepository;
    private final UserService userService;
    private final NoteMapper noteMapper;

    public void createNote(NoteDTO noteDTO) {
        Note note = noteMapper.toNote(noteDTO);
        User user = userService.getCurrentUser();
        note.setUser(user);

        noteRepository.save(note);
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
        String userEmail = userService.getCurrentUserUsername();
        List<Note> notes = noteRepository
                .findAllByUserEmailOrderByLastModifiedDateDesc(userEmail);

        return noteMapper.toNoteDTOList(notes);
    }

    public List<NoteDTO> searchNotes(String keyword) {
        String userEmail = userService.getCurrentUserUsername();
        List<Note> notes = noteRepository
                .findByTitleOrContentContainingAndUserEmailOrderByDesc(keyword, userEmail);

        return noteMapper.toNoteDTOList(notes);
    }


}

