package com.mikhalov.taskonaut.controller;

import com.mikhalov.taskonaut.dto.NotebookDTO;
import com.mikhalov.taskonaut.service.NotesManageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/labels")
public class NotebookController {

    private final NotesManageService notesManageService;

    @PostMapping()
    public RedirectView createLabel(@ModelAttribute NotebookDTO label, @RequestParam("noteId") String noteId) {
        log.info("creating new label {} for note {}id", label, noteId);
        notesManageService.createNotebook(label, noteId);

        return new RedirectView("/notes");
    }

    @GetMapping()
    public ModelAndView getAllLabels(ModelAndView modelAndView) {
        log.info("get all labels");

        return modelAndView;
    }
}
