package com.mikhalov.taskonaut.controller;

import com.mikhalov.taskonaut.dto.LabelDTO;
import com.mikhalov.taskonaut.dto.SortAndPageDTO;
import com.mikhalov.taskonaut.service.LabelService;
import com.mikhalov.taskonaut.service.NoteService;
import com.mikhalov.taskonaut.util.ModelAndViewUtil;
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
public class LabelController {

    private final ModelAndViewUtil modelAndViewUtil;
    private final LabelService labelService;
    private final NoteService noteService;


    @PostMapping
    public RedirectView createLabel(@ModelAttribute LabelDTO label, @RequestParam("noteId") String noteId) {
        log.info("creating new label {} for note {}id", label, noteId);
        labelService.createLabel(label, noteId);

        return new RedirectView("/notes");
    }

    @PutMapping
    public RedirectView addNoteToLabel(@RequestParam(name = "labelId") String labelId,
                                       @RequestParam(name = "noteId") String noteId) {
        log.info("adding note '{}' to label '{}'", noteId, labelId);
        labelService.addNoteToLabel(noteId, labelId);

        return new RedirectView("/notes");
    }

    @GetMapping("/{name}")
    public ModelAndView getSortedNotesByLabelName(@ModelAttribute SortAndPageDTO sortAndPage,
                                                  @PathVariable String name,
                                                  ModelAndView modelAndView) {
        log.info("getting label '{}'\nSorting params: {}, {}",
                name, sortAndPage, sortAndPage.isAsc() ? "asc" : "desc");
        var notesByLabelName = noteService.getSortedNotesByLabelName(name, sortAndPage);
        log.info("successful done");

        return modelAndViewUtil
                .getPagingModelAndView(modelAndView, notesByLabelName, sortAndPage, "/label");
    }


}
