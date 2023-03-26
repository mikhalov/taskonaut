package com.mikhalov.taskonaut.controller;

import com.mikhalov.taskonaut.bean.NoteData;
import com.mikhalov.taskonaut.model.Note;
import com.mikhalov.taskonaut.service.NoteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

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
    public ModelAndView getAllNotes(ModelAndView modelAndView) {
        log.info("getting all");
        List<Note> notes = noteService.getAllNotes();
        modelAndView.addObject("note", new NoteData());
        modelAndView.addObject("notes", notes);
        return modelAndView;
    }


    @PostMapping()
    public RedirectView createNote(@ModelAttribute NoteData noteData) {
        log.info("creating new note {}", noteData);
        Note createdNote = noteService.createNote(new Note(noteData.getTitle(), noteData.getContent()));
        return new RedirectView("/notes");
    }

    @GetMapping("/{id}")
    public ModelAndView getNoteById(@PathVariable String id, ModelAndView modelAndView) {
        log.info("getting by {}id", id);
        Note note = noteService.getNoteById(id).orElseThrow(() -> new EntityNotFoundException("Note not found with id: " + id));
        modelAndView.addObject("note", note);
        modelAndView.addObject("id", id);
        modelAndView.setViewName("fragments/form-fragment");
        return modelAndView;
    }


    @PutMapping("/{id}")
    public RedirectView  updateNote(@PathVariable String id, @ModelAttribute Note note) {
        log.info("updating note {}id", id);
        Note updatedNote = noteService.updateNote(id, note);
        return new RedirectView("/notes");
    }

    @DeleteMapping("/{id}")
    public RedirectView  deleteNote(@PathVariable String id) {
        log.info("deleting note {}id", id);
        noteService.deleteNote(id);
        return new RedirectView("/notes");
    }
}