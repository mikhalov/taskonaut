package com.mikhalov.taskonaut.util;

import com.mikhalov.taskonaut.dto.LabelDTO;
import com.mikhalov.taskonaut.dto.NoteDTO;
import com.mikhalov.taskonaut.dto.SortAndPageDTO;
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
                                              SortAndPageDTO sortAndPage,
                                              String viewName) {
        List<LabelDTO> labels = labelService.getAllLabels();
        sortAndPage.setTotalPages(notesDTO.getTotalPages());

        modelAndView.addObject("labels", labels);
        modelAndView.addObject("note", new NoteDTO());
        modelAndView.addObject("label", new LabelDTO());
        modelAndView.addObject("notes", notesDTO.getContent());
        modelAndView.addObject("sortAndPage", sortAndPage);
        modelAndView.setViewName(viewName);

        return modelAndView;
    }

}
