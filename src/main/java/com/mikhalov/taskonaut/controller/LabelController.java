package com.mikhalov.taskonaut.controller;

import com.mikhalov.taskonaut.dto.LabelDTO;
import com.mikhalov.taskonaut.dto.NoteDTO;
import com.mikhalov.taskonaut.service.LabelService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/labels")
public class LabelController {

    private static final String LABELS_LITERAL = "labels";
    private final LabelService labelService;


    @PostMapping
    public RedirectView createLabel(@ModelAttribute LabelDTO label, @RequestParam("noteId") String noteId) {
        log.info("creating new label {} for note {}id", label, noteId);
        labelService.createLabel(label, noteId);

        return new RedirectView("/notes");
    }

    @PutMapping
    public RedirectView addNote(
            @RequestParam(name = "labelId") String labelId,
            @RequestParam(name = "noteId") String noteId
    ) {
        labelService.addNoteToLabel(noteId, labelId);

        return new RedirectView("/notes");
    }

    @GetMapping
    public ModelAndView getAllLabels(ModelAndView modelAndView) {
        log.info("get all labels");
        List<LabelDTO> labels = labelService.getAllLabels();
        modelAndView.addObject(LABELS_LITERAL, labels);
        modelAndView.addObject("note", new NoteDTO());
        modelAndView.setViewName(LABELS_LITERAL);

        return modelAndView;
    }

    @GetMapping("/{name}")
    public ModelAndView getLabelByName(@PathVariable String name, ModelAndView modelAndView) {
        List<NoteDTO> notesByLabelName = labelService.getAllNotesByLabelName(name);
        List<LabelDTO> labels = labelService.getAllLabels();
        modelAndView.addObject(LABELS_LITERAL, labels);
        modelAndView.addObject("note", new NoteDTO());
        modelAndView.addObject("label", new LabelDTO());
        modelAndView.addObject("notes", notesByLabelName);
        modelAndView.setViewName("/label");

        return modelAndView;
    }

}
