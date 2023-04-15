package com.mikhalov.taskonaut.controller;

import com.mikhalov.taskonaut.dto.NoteDTO;
import com.mikhalov.taskonaut.model.enums.NoteSortOption;
import com.mikhalov.taskonaut.service.NoteService;
import com.mikhalov.taskonaut.util.ModelAndViewUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
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

    private static final String MAIN_NOTES_PAGE_VIEW = "/notes";
    private final ModelAndViewUtil modelAndViewUtil;
    private final NoteService noteService;

    @GetMapping()
    public ModelAndView getSortedNotes(@RequestParam(value = "sort", required = false, defaultValue = "LAST_MODIFIED") NoteSortOption sortBy,
                                       @RequestParam(value = "asc", required = false, defaultValue = "false") boolean ascending,
                                       @RequestParam(value = "page", defaultValue = "0") int page,
                                       @RequestParam(value = "size", defaultValue = "20") int size,
                                       ModelAndView modelAndView) {
        log.info("getting sorted notes by {}, {}",
                sortBy, ascending ? "asc" : "desc");
        Page<NoteDTO> sortedNotes = noteService.getSortedNotes(sortBy, ascending, page, size);
        log.info("successful done");
        return modelAndViewUtil
                .getPagingModelAndView(modelAndView, sortedNotes, sortBy, ascending, MAIN_NOTES_PAGE_VIEW);
    }



    @GetMapping("/search")
    public ModelAndView searchNotes(@RequestParam(value = "sort", required = false) NoteSortOption sortBy,
                                    @RequestParam(value = "asc", required = false) boolean ascending,
                                    @RequestParam("keyword") String keyword,
                                    ModelAndView modelAndView) {
        log.info("search foundNotes by title and content that contains '{}', sort by {} {}",
                keyword, sortBy, ascending ? "asc" : "desc");
        List<NoteDTO> foundNotes = noteService.searchNotesByKeywordAndSort(keyword, sortBy, ascending);
        log.info("successful search");

        return modelAndViewUtil
                .getMainModelAndView(modelAndView, foundNotes, ascending, MAIN_NOTES_PAGE_VIEW);
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
