package com.mikhalov.taskonaut.service;

import com.mikhalov.taskonaut.model.Notebook;
import com.mikhalov.taskonaut.repository.NotebookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotebookService {

    private final NotebookRepository notebookRepository;

    public Notebook createNotebook(Notebook label) {
        return notebookRepository.save(label);
    }

    public List<Notebook> getAllLabels() {
        return notebookRepository.findAll();
    }

    public void updateNotebookName() {

    }


    public void deleteNotebook() {

    }

    public void findByIdNotebook() {

    }

}
