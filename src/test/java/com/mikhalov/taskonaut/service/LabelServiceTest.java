package com.mikhalov.taskonaut.service;

import com.mikhalov.taskonaut.dto.LabelDTO;
import com.mikhalov.taskonaut.mapper.LabelMapper;
import com.mikhalov.taskonaut.model.Label;
import com.mikhalov.taskonaut.model.Note;
import com.mikhalov.taskonaut.model.User;
import com.mikhalov.taskonaut.repository.LabelRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LabelServiceTest {

    @InjectMocks
    private LabelService labelService;

    @Mock
    private LabelRepository labelRepository;

    @Mock
    private NoteService noteService;

    @Mock
    private UserService userService;

    @Mock
    private LabelMapper labelMapper;

    @Test
    void testCreateLabel() {
        LabelDTO labelDTO = mock(LabelDTO.class);
        Note note = mock(Note.class);
        User user = mock(User.class);
        String noteId = "noteId";
        String userEmail = "user@example.com";

        when(labelDTO.getName()).thenReturn("labelName");
        when(noteService.getNoteById(noteId)).thenReturn(note);
        when(userService.getCurrentUser()).thenReturn(user);
        when(user.getEmail()).thenReturn(userEmail);
        when(note.getUser()).thenReturn(user);
        when(labelMapper.toLabel(labelDTO)).thenReturn(new Label());
        when(labelRepository.findByNameAndUserEmail("labelName", userEmail)).thenReturn(Optional.empty());

        labelService.createLabel(labelDTO, noteId);

        verify(noteService, times(1)).getNoteById(noteId);
        verify(userService, times(1)).getCurrentUser();
        verify(labelMapper, times(1)).toLabel(labelDTO);
        verify(labelRepository, times(1)).save(any(Label.class));
    }

    @Test
    void testCreateLabel_whenLabelExists() {
        LabelDTO labelDTO = mock(LabelDTO.class);
        Note note = mock(Note.class);
        User user = mock(User.class);
        String noteId = "noteId";
        String userEmail = "user@example.com";
        Label existingLabel = mock(Label.class);

        when(labelDTO.getName()).thenReturn("labelName");
        when(noteService.getNoteById(noteId)).thenReturn(note);
        when(userService.getCurrentUser()).thenReturn(user);
        when(user.getEmail()).thenReturn(userEmail);
        when(note.getUser()).thenReturn(user);
        when(labelRepository.findByNameAndUserEmail("labelName", userEmail)).thenReturn(Optional.of(existingLabel));

        labelService.createLabel(labelDTO, noteId);

        verify(noteService, times(1)).getNoteById(noteId);
        verify(userService, times(1)).getCurrentUser();
        verify(labelMapper, times(0)).toLabel(labelDTO);
    }

    @Test
    void testAddNoteToLabel() {
        String noteId = "noteId";
        String labelId = "labelId";
        Note note = mock(Note.class);
        Label label = mock(Label.class);

        when(noteService.getNoteById(noteId)).thenReturn(note);
        when(labelRepository.findById(labelId)).thenReturn(Optional.of(label));
        doNothing().when(label).addNote(note);
        when(labelRepository.save(label)).thenReturn(label);

        labelService.addNoteToLabel(noteId, labelId);

        verify(noteService, times(1)).getNoteById(noteId);
        verify(labelRepository, times(1)).findById(labelId);
        verify(label, times(1)).addNote(note);
        verify(labelRepository, times(1)).save(label);
    }

    @Test
    void testGetAllLabels() {
        String email = "user@example.com";
        List<Label> labels = List.of(mock(Label.class), mock(Label.class));

        when(userService.getCurrentUserUsername()).thenReturn(email);
        when(labelRepository.findAllByUserEmail(email)).thenReturn(labels);
        when(labelMapper.toLabelDTO(any(Label.class))).thenReturn(mock(LabelDTO.class));

        List<LabelDTO> labelDTOs = labelService.getAllLabels();

        assertEquals(labels.size(), labelDTOs.size());
        verify(userService, times(1)).getCurrentUserUsername();
        verify(labelRepository, times(1)).findAllByUserEmail(email);
        verify(labelMapper, times(labels.size())).toLabelDTO(any(Label.class));
    }

    @Test
    void testDeleteLabel() {
        String labelId = "labelId";
        Label label = mock(Label.class);
        Note note = mock(Note.class);
        List<Note> notes = List.of(note);

        when(labelRepository.findById(labelId)).thenReturn(Optional.of(label));
        when(label.getNotes()).thenReturn(notes);
        doNothing().when(label).removeNote(note);
        doNothing().when(noteService).updateNote(note);
        doNothing().when(labelRepository).delete(label);

        labelService.deleteLabel(labelId);

        verify(labelRepository, times(1)).findById(labelId);
        verify(label, times(1)).removeNote(note);
        verify(noteService, times(1)).updateNote(note);
        verify(labelRepository, times(1)).delete(label);
    }

}