package com.mikhalov.taskonaut.service;

import com.mikhalov.taskonaut.model.Label;
import com.mikhalov.taskonaut.repository.LabelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LabelService {

    private final LabelRepository labelRepository;

    public Label createLabel(Label label) {
        System.out.println(label);
        return labelRepository.save(label);
    }

    public List<Label> getAllLabels() {
        return labelRepository.findAll();
    }

    public Label getById(String id) {
        return labelRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Label not found with id: " + id));
    }

}
