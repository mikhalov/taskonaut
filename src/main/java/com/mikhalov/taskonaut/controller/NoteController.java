package com.mikhalov.taskonaut.controller;

import com.mikhalov.taskonaut.model.Note;
import com.mikhalov.taskonaut.service.NoteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
    public String getAllNotes(Model model) {
        log.info("getting all");
        List<Note> notes = noteService.getAllNotes();
        model.addAttribute("notes", notes);
        return "notes";
    }

    @GetMapping("/new")
    public String fillNew(Model model) {
        log.info("return empty node");
        model.addAttribute("note", new Note());
        return "form";
    }

    @GetMapping("/{id}")
    public String getNoteById(@PathVariable String id, Model model) {
        log.info("getting by {}id", id);
        Note note = noteService.getNoteById(id).orElseThrow(() -> new EntityNotFoundException("Note not found with id: " + id));
        model.addAttribute("note", note);
        return "view";
    }

    @PostMapping()
    public String createNote(Note note) {
        log.info("creating note {}id", note.getId());
        Note createdNote = noteService.createNote(note);
        return "redirect:/notes";
    }

    @GetMapping("/{id}/edit")
    public String editNote(@PathVariable String id, Model model) {
        log.info("editing note {}id", id);
        Note note = noteService.getNoteById(id).orElseThrow(() -> new EntityNotFoundException("Note not found with id: " + id));
        model.addAttribute("note", note);
        return "form";
    }

    @PostMapping("/{id}")
    public String updateNote(@PathVariable String id, Note note) {
        log.info("updating note {}id", id);
        Note updatedNote = noteService.updateNote(id, note);
        return "redirect:/notes";
    }

    //    @GetMapping("/{id}/delete")
//    public String deleteNote(@PathVariable String id) {
//        noteService.deleteNote(id);
//        return "redirect:/notes";
//    }
    @DeleteMapping("/{id}")
    public String deleteNote(@PathVariable String id) {
        log.info("deleting note {}id", id);
        noteService.deleteNote(id);
        return "redirect:/notes";
    }
}