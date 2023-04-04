package com.mikhalov.taskonaut.service;

import com.mikhalov.taskonaut.dto.LabelDTO;
import com.mikhalov.taskonaut.dto.NoteDTO;
import com.mikhalov.taskonaut.mapper.LabelMapper;
import com.mikhalov.taskonaut.mapper.NoteMapper;
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
    private final UserService userService;
    private final LabelMapper labelMapper;
    private final NoteMapper noteMapper;

    public void createLabel(LabelDTO labelDTO, String noteId) {
        Label label = getLabelByNameForCurrentUser(labelDTO.getName(), userService.getCurrentUserUsername())
                .orElseGet(() -> {
                    Label newLabel = labelMapper.toLabel(labelDTO);
                    newLabel.setUser(userService.getCurrentUser());
                    return labelRepository.save(newLabel);
                });

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
        String email = userService.getCurrentUserUsername();

        return labelRepository.findAllByUserEmail(email)
                .stream()
                .map(labelMapper::toLabelDTO)
                .toList();
    }
    public Optional<Label> getLabelByNameForCurrentUser(String name, String userEmail) {
        return labelRepository.findByNameAndUserEmail(name, userEmail);
    }

    public Label getById(String id) {
        return labelRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Label not found with id: " + id));
    }

    public List<NoteDTO> getAllNotesByLabelName(String name) {
        Label label = labelRepository.findByNameAndUserEmail(name, userService.getCurrentUserUsername())
                .orElseThrow(() -> new EntityNotFoundException("Label not found with name: " + name));
        return label.getNotes()
                .stream()
                .map(noteMapper::toNoteDTO)
                .toList();
    }
}
