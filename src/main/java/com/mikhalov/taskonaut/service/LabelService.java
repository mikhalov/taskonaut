package com.mikhalov.taskonaut.service;

import com.mikhalov.taskonaut.dto.LabelDTO;
import com.mikhalov.taskonaut.dto.NoteDTO;
import com.mikhalov.taskonaut.mapper.LabelMapper;
import com.mikhalov.taskonaut.model.Label;
import com.mikhalov.taskonaut.model.Note;
import com.mikhalov.taskonaut.repository.LabelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LabelService {

    private final LabelRepository labelRepository;
    private final NoteService noteService;
    private final LabelMapper labelMapper;

    public void createLabel(LabelDTO labelDTO, String noteId) {
        Label label = getLabelByName(labelDTO.getName())
                .orElseGet(() -> labelRepository.save(labelMapper.toLabel(labelDTO)));

        addNoteToLabel(noteId, label);
    }

    public void addNoteToLabel(String noteId, String labelId) {
        Label label = getById(labelId);
        addNoteToLabel(noteId, label);
    }

    private void addNoteToLabel(String noteId, Label label) {
        Note note = noteService.getNoteById(noteId);
        note.setLabel(label);
        noteService.updateNote(note);
    }

    public List<LabelDTO> getAllLabels() {
        return labelRepository.findAll()
                .stream()
                .map(labelMapper::toLabelDTO)
                .toList();
    }
    public Optional<Label> getLabelByName(String name) {
        return labelRepository.findByName(name);
    }

    public Label getById(String id) {
        return labelRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Label not found with id: " + id));
    }

    public List<NoteDTO> getAllNotesByLabelName(String name) {
        return noteService.getAllByLabelName(name);
    }
}
