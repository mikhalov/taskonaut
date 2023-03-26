package com.mikhalov.taskonaut.controller;

import com.mikhalov.taskonaut.bean.NoteData;
import com.mikhalov.taskonaut.model.Note;
import com.mikhalov.taskonaut.service.NoteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/notes")
public class NoteController {

    private final NoteService noteService;


    public NoteController(@Autowired NoteService noteService) {
        this.noteService = noteService;
    }

    @GetMapping
    public ModelAndView getAllNotes(ModelAndView model) {
        log.info("getting all");
        List<Note> notes = noteService.getAllNotes();
        model.addObject("note", new NoteData());
        model.addObject("notes", notes);
        return model;
    }


    @PostMapping()
    public String createNote(@ModelAttribute NoteData noteData) {
        log.info("creating new note {}", noteData);
        Note createdNote = noteService.createNote(new Note(noteData.getTitle(), noteData.getContent()));
        return "redirect:/notes";
    }

    @GetMapping("/{id}")
    public ModelAndView getNoteById(@PathVariable String id, ModelAndView model) {
        log.info("getting by {}id", id);
        Note note = noteService.getNoteById(id).orElseThrow(() -> new EntityNotFoundException("Note not found with id: " + id));
        model.addObject("note", note);
        model.setViewName("fragments/form-fragment");
        return model;
    }


    @PostMapping("/{id}")
    public String updateNote(@PathVariable String id, Note note) {
        log.info("updating note {}id", id);
        Note updatedNote = noteService.updateNote(id, note);
        return "redirect:/notes";
    }

    @DeleteMapping("/{id}")
    public String deleteNote(@PathVariable String id) {
        log.info("deleting note {}id", id);
        noteService.deleteNote(id);
        return "redirect:/notes";
    }
}