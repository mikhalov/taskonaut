package com.mikhalov.taskonaut.service;

import com.mikhalov.taskonaut.dto.NoteDTO;
import com.mikhalov.taskonaut.dto.NotebookDTO;
import com.mikhalov.taskonaut.mapper.NoteManageMapper;
import com.mikhalov.taskonaut.model.Note;
import com.mikhalov.taskonaut.model.Notebook;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotesManageService {

    private final NoteService noteService;
    private final NotebookService notebookService;
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

    public void createNotebook(NotebookDTO labelDTO, String noteId) {
        Notebook label = notebookService.createNotebook(noteManageMapper.toLabel(labelDTO));
        Note note = noteService.getNoteById(noteId);
        note.setNotebook(label);
        noteService.updateNote(note);

    }

    public List<NotebookDTO> getAllLabels() {
        return noteManageMapper.toNotebookDTOList(notebookService.getAllLabels());
    }
}
