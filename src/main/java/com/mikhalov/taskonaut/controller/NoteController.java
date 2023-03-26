package com.mikhalov.taskonaut.controller;

import com.mikhalov.taskonaut.bean.NoteDTO;
import com.mikhalov.taskonaut.service.NoteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

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
        List<NoteDTO> notes = noteService.getAllNotes();
        modelAndView.addObject("note", new NoteDTO());
        modelAndView.addObject("notes", notes);
        return modelAndView;
    }


    @PostMapping()
    public RedirectView createNote(@ModelAttribute NoteDTO noteDTO) {
        log.info("creating new note {}", noteDTO);
        noteService.createNote(noteDTO);
        return new RedirectView("/notes");
    }

    @GetMapping("/{id}")
    public ModelAndView getNoteById(@PathVariable String id, ModelAndView modelAndView) {
        log.info("getting by {}id", id);
        NoteDTO note = noteService.getNoteById(id);
        modelAndView.addObject("note", note);
        modelAndView.setViewName("fragments/form-fragment");
        return modelAndView;
    }


    @PutMapping("/{id}")
    public RedirectView updateNote(@PathVariable String id, @ModelAttribute NoteDTO noteDTO) {
        log.info("updating note {}id", id);
        noteService.updateNote(id, noteDTO);
        return new RedirectView("/notes");
    }

    @DeleteMapping("/{id}")
    public RedirectView deleteNote(@PathVariable String id) {
        log.info("deleting note {}id", id);
        noteService.deleteNote(id);
        return new RedirectView("/notes");
    }
}