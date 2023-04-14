package com.mikhalov.taskonaut.util;

import com.mikhalov.taskonaut.dto.LabelDTO;
import com.mikhalov.taskonaut.dto.NoteDTO;
import com.mikhalov.taskonaut.dto.NoteSortingDTO;
import com.mikhalov.taskonaut.model.enums.NoteSortOption;
import com.mikhalov.taskonaut.service.LabelService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ModelAndViewUtil {

    private final LabelService labelService;

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
                new NoteSortingDTO(NoteSortOption.values(), ascending)
        );
        modelAndView.setViewName(viewName);

        return modelAndView;
    }
}
