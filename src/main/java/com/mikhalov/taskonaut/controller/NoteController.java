package com.mikhalov.taskonaut.controller;

import com.mikhalov.taskonaut.dto.ExportParamsDTO;
import com.mikhalov.taskonaut.dto.LabelDTO;
import com.mikhalov.taskonaut.dto.NoteDTO;
import com.mikhalov.taskonaut.dto.SortAndPageDTO;
import com.mikhalov.taskonaut.service.NoteService;
import com.mikhalov.taskonaut.service.PdfExportService;
import com.mikhalov.taskonaut.util.ModelAndViewUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.validation.Valid;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/notes")
@RequiredArgsConstructor
public class NoteController {

    private static final String MAIN_NOTES_PAGE_VIEW = "/notes";
    private final ModelAndViewUtil modelAndViewUtil;
    private final PdfExportService pdfExportService;
    private final NoteService noteService;


    @GetMapping()
    public ModelAndView getSortedNotes(@ModelAttribute @Valid SortAndPageDTO sortAndPage,
                                       ModelAndView modelAndView) {
        log.info("getting sorted notes.");
        List<LabelDTO> labels = modelAndViewUtil.getAllLabels();
        Page<NoteDTO> sortedNotes = noteService.getSortedNotes(sortAndPage);
        log.info("Successful sorted. \nParams: {}, {}",
                sortAndPage, sortAndPage.isAsc() ? "asc" : "desc");

        return modelAndViewUtil.getPagingModelAndView(modelAndView, labels,
                sortedNotes, sortAndPage, MAIN_NOTES_PAGE_VIEW);
    }


    @GetMapping("/search")
    public ModelAndView searchNotes(@ModelAttribute @Valid SortAndPageDTO sortAndPage,
                                    @RequestParam("keyword") String keyword,
                                    ModelAndView modelAndView) {
        log.info("search foundNotes by title and content that contains '{}'\nSorting params: {}, {}",
                keyword, sortAndPage, sortAndPage.isAsc() ? "asc" : "desc");

        List<LabelDTO> labels = modelAndViewUtil.getAllLabels();
        Page<NoteDTO> foundNotes = noteService.searchNotesByKeywordAndSort(keyword, sortAndPage);
        log.info("successful search");

        return modelAndViewUtil
                .getPagingModelAndView(modelAndView, labels, foundNotes, sortAndPage, MAIN_NOTES_PAGE_VIEW);
    }

    @GetMapping(value = "/export/pdf", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> exportNotesToPdf(@ModelAttribute ExportParamsDTO exportParams) {

        log.info("user request to export all notes to PDF and download\n" +
                "{}", exportParams);
        List<NoteDTO> notes = noteService.getSortedNotesForExport(exportParams);

        try {
            byte[] pdfContent = pdfExportService.exportNotesToPdf(notes, exportParams);
            String currentDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yy.MM.dd_HH-mm"));
            return ResponseEntity.ok()
                    .header("Content-Disposition",
                            String.format("attachment; filename=notes_%s.pdf", currentDate))
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(pdfContent);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
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
    public ResponseEntity<String> deleteNote(@PathVariable String id) {
        log.info("deleting note {}id", id);
        noteService.deleteNote(id);

        return ResponseEntity.ok("Delete successful");
    }
}
