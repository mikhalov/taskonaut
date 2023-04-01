package com.mikhalov.taskonaut.service;

import com.mikhalov.taskonaut.dto.LabelDTO;
import com.mikhalov.taskonaut.dto.NoteDTO;
import com.mikhalov.taskonaut.mapper.NoteManageMapper;
import com.mikhalov.taskonaut.model.Label;
import com.mikhalov.taskonaut.model.Note;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotesManageService {

    private final NoteService noteService;
    private final LabelService labelService;
    private final NoteManageMapper noteManageMapper;

    public void createNote(NoteDTO noteDTO) {
        noteService.createNote(noteManageMapper.toNote(noteDTO));
    }

    public void updateNote(NoteDTO noteDTO) {
        Note note = noteService.getNoteById(noteDTO.getId());
        noteManageMapper.updateNote(noteDTO, note);
        noteService.updateNote(note);
    }

    public void deleteNote(String id) {
        noteService.deleteNote(id);
    }

    public NoteDTO getNoteById(String id) {
        return noteManageMapper.toNoteData(noteService.getNoteById(id));
    }

    public List<NoteDTO> getAllNotes() {
        return noteManageMapper.toNoteDTOList(noteService.getAllNotes());
    }

    public void createLabel(LabelDTO labelDTO, String noteId) {
        labelService.isLabelAlreadyExist(labelDTO.getName())
                .ifPresentOrElse(
                        label -> addNoteToLabel(noteId, label),
                        () -> {
                            Label label = labelService.createLabel(noteManageMapper.toLabel(labelDTO));
                            addNoteToLabel(noteId, label);
                        });
    }

    public void addNoteToLabel(String noteId, String labelId) {
        Label label = labelService.getById(labelId);
        addNoteToLabel(noteId, label);
    }

    private void addNoteToLabel(String noteId, Label label) {
        Note note = noteService.getNoteById(noteId);
        note.setLabel(label);
        noteService.updateNote(note);
    }

    public List<LabelDTO> getAllLabels() {
        return noteManageMapper.toLabelDTOList(labelService.getAllLabels());
    }

    public List<NoteDTO> getAllNotesByLabelName(String labelName) {
        return noteManageMapper.toNoteDTOList(noteService.getAllByLabelName(labelName));
    }
}
