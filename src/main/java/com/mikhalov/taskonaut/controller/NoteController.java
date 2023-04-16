package com.mikhalov.taskonaut.controller;

import com.mikhalov.taskonaut.dto.NoteDTO;
import com.mikhalov.taskonaut.dto.SortAndPageDTO;
import com.mikhalov.taskonaut.service.NoteService;
import com.mikhalov.taskonaut.util.ModelAndViewUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

@Slf4j
@Controller
@RequestMapping("/notes")
@RequiredArgsConstructor
public class NoteController {

    private static final String MAIN_NOTES_PAGE_VIEW = "/notes";
    private final ModelAndViewUtil modelAndViewUtil;
    private final NoteService noteService;

    @GetMapping()
    public ModelAndView getSortedNotes(@ModelAttribute SortAndPageDTO sortAndPage,
                                       ModelAndView modelAndView) {
        log.info("getting sorted notes.");
        Page<NoteDTO> sortedNotes = noteService.getSortedNotes(sortAndPage);
        log.info("Successful sorted. \nParams: {}, {}",
                sortAndPage, sortAndPage.isAsc() ? "asc" : "desc");

        return modelAndViewUtil.getPagingModelAndView(modelAndView,
                sortedNotes, sortAndPage, MAIN_NOTES_PAGE_VIEW);
    }


    @GetMapping("/search")
    public ModelAndView searchNotes(@ModelAttribute SortAndPageDTO sortAndPage,
                                    @RequestParam("keyword") String keyword,
                                    ModelAndView modelAndView) {
        log.info("search foundNotes by title and content that contains '{}'\nSorting params: {}, {}",
                keyword, sortAndPage, sortAndPage.isAsc() ? "asc" : "desc");
        Page<NoteDTO> foundNotes = noteService.searchNotesByKeywordAndSort(keyword, sortAndPage);
        log.info("successful search");

        return modelAndViewUtil
                .getPagingModelAndView(modelAndView, foundNotes, sortAndPage, MAIN_NOTES_PAGE_VIEW);
    }


    @PostMapping()
    public RedirectView createNote(@ModelAttribute NoteDTO noteDTO) {
        log.info("creating new note {}", noteDTO);
        noteService.createNote(noteDTO);
        return new RedirectView(MAIN_NOTES_PAGE_VIEW);
    }

    @GetMapping("/{id}")
    public ModelAndView getNoteById(@PathVariable String id, ModelAndView modelAndView) {
        log.info("getting by {}id", id);
        NoteDTO note = noteService.getNoteDTOById(id);
        log.info("label {}", note.getLabelDTO());
        modelAndView.addObject("note", note);
        modelAndView.setViewName("fragments/form-fragment");

        return modelAndView;
    }


    @PutMapping("/{id}")
    public RedirectView updateNote(@ModelAttribute NoteDTO noteDTO) {
        log.info("updating note {}", noteDTO);
        noteService.updateNote(noteDTO);

        return new RedirectView(MAIN_NOTES_PAGE_VIEW);
    }

    @DeleteMapping("/{id}")
    public RedirectView deleteNote(@PathVariable String id) {
        log.info("deleting note {}id", id);
        noteService.deleteNote(id);

        return new RedirectView(MAIN_NOTES_PAGE_VIEW);
    }
}
