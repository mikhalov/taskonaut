package com.mikhalov.taskonaut.service;

import com.mikhalov.taskonaut.dto.LabelDTO;
import com.mikhalov.taskonaut.mapper.LabelMapper;
import com.mikhalov.taskonaut.model.Label;
import com.mikhalov.taskonaut.model.Note;
import com.mikhalov.taskonaut.model.User;
import com.mikhalov.taskonaut.repository.LabelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LabelService {

    private final LabelRepository labelRepository;
    private final NoteService noteService;
    private final UserService userService;
    private final LabelMapper labelMapper;

    public void createLabel(LabelDTO labelDTO, String noteId) {
        Note note = noteService.getNoteById(noteId);
        User user = userService.getCurrentUser();
        String email = user.getEmail();
        if (!note.getUser().getEmail().equals(email)) {
            throw new SecurityException("The current user does not have permission to add a label to this note");
        }

        Label label = getLabelByNameForCurrentUser(labelDTO.getName(), email)
                .orElseGet(() -> {
                    Label newLabel = labelMapper.toLabel(labelDTO);
                    newLabel.setUser(user);
                    return newLabel;
                });

        addNoteToLabel(note, label);

    }

    public void addNoteToLabel(String noteId, String labelId) {
        Note note = noteService.getNoteById(noteId);
        Label label = getById(labelId);
        addNoteToLabel(note, label);
    }

    private void addNoteToLabel(Note note, Label label) {
        label.addNote(note);
        labelRepository.save(label);
    }

    public List<LabelDTO> getAllLabels() {
        String email = userService.getCurrentUserUsername();
        List<Label> labels = labelRepository.findAllByUserEmail(email);

        return labelMapper.toLabelDTOList(labels);
    }

    @Transactional
    public void deleteLabel(String labelId) {
        Label label = getById(labelId);

        List<Note> notes = new ArrayList<>(label.getNotes());
        for (Note note : notes) {
            label.removeNote(note);
            noteService.updateNote(note);
        }

        labelRepository.delete(label);
    }

    private Optional<Label> getLabelByNameForCurrentUser(String name, String userEmail) {
        return labelRepository.findByNameAndUserEmail(name, userEmail);
    }

    private Label getById(String id) {
        return labelRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Label not found with id: " + id));
    }

    public List<LabelDTO> getAllLabelsByChatId(Long chatId) {
        List<Label> labels = labelRepository.findAllByUserTelegramChatIdOrderByName(chatId);

        return labelMapper.toLabelDTOList(labels);
    }
}
