package com.mikhalov.taskonaut.controller;

import com.mikhalov.taskonaut.dto.LabelDTO;
import com.mikhalov.taskonaut.dto.NoteDTO;
import com.mikhalov.taskonaut.service.LabelService;
import com.mikhalov.taskonaut.service.NoteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/notes")
@RequiredArgsConstructor
public class NoteController {

    private final NoteService noteService;
    private final LabelService labelService;

    @GetMapping
    public ModelAndView getAllNotes(ModelAndView modelAndView) {
        log.info("getting all");
        List<NoteDTO> notes = noteService.getAllNotes();
        List<LabelDTO> labels = labelService.getAllLabels();
        modelAndView.addObject("labels", labels);
        modelAndView.addObject("note", new NoteDTO());
        modelAndView.addObject("label", new LabelDTO());
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
        NoteDTO note = noteService.getNoteDTOById(id);
        modelAndView.addObject("note", note);
        modelAndView.setViewName("fragments/form-fragment");

        return modelAndView;
    }


    @PutMapping("/{id}")
    public RedirectView updateNote(@ModelAttribute NoteDTO noteDTO) {
        log.info("updating note {}", noteDTO);
        noteService.updateNote(noteDTO);

        return new RedirectView("/notes");
    }

    @DeleteMapping("/{id}")
    public RedirectView deleteNote(@PathVariable String id) {
        log.info("deleting note {}id", id);
        noteService.deleteNote(id);

        return new RedirectView("/notes");
    }
}
