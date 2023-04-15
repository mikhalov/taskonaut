package com.mikhalov.taskonaut.util;

import com.mikhalov.taskonaut.dto.LabelDTO;
import com.mikhalov.taskonaut.dto.NoteDTO;
import com.mikhalov.taskonaut.dto.NoteSortingDTO;
import com.mikhalov.taskonaut.model.enums.NoteSortOption;
import com.mikhalov.taskonaut.service.LabelService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ModelAndViewUtil {

    private final LabelService labelService;

    public ModelAndView getPagingModelAndView(ModelAndView modelAndView,
                                              Page<NoteDTO> notesDTO,
                                              NoteSortOption sortBy,
                                              boolean ascending,
                                              String viewName) {
        List<LabelDTO> labels = labelService.getAllLabels();
        modelAndView.addObject("labels", labels);
        modelAndView.addObject("note", new NoteDTO());
        modelAndView.addObject("label", new LabelDTO());
        modelAndView.addObject("notes", notesDTO.getContent());
        modelAndView.addObject("noteSortingDTO",
                NoteSortingDTO.builder()
                        .sortOptions(NoteSortOption.values())
                        .currentSorting(sortBy)
                        .ascending(ascending)
                        .build()
        );
        modelAndView.addObject("currentPage", notesDTO.getNumber());
        modelAndView.addObject("totalPages", notesDTO.getTotalPages());
        modelAndView.setViewName(viewName);

        return modelAndView;
    }

    public ModelAndView getMainModelAndView(ModelAndView modelAndView,
                                            List<NoteDTO> notesDTO,
                                            boolean ascending,
                                            String viewName) {
        List<LabelDTO> labels = labelService.getAllLabels();
        modelAndView.addObject("labels", labels);
        modelAndView.addObject("note", new NoteDTO());
        modelAndView.addObject("label", new LabelDTO());
        modelAndView.addObject("notes", notesDTO);
        modelAndView.addObject(
                "noteSortingDTO",
                NoteSortingDTO.builder()
                        .sortOptions(NoteSortOption.values())
                        .ascending(ascending)
                        .build()
        );
        modelAndView.setViewName(viewName);

        return modelAndView;
    }
}
