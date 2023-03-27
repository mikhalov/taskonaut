package com.mikhalov.taskonaut.service;

import com.mikhalov.taskonaut.dto.NotebookDTO;
import com.mikhalov.taskonaut.repository.NotebookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotebookService {

    private final NotebookRepository notebookRepository;

    public NotebookDTO createNotebook(){
        return null;
    }

    public void updateNotebookName(){

    }

    public void addNote() {

    }

    public void deleteNote() {

    }

    public void deleteNotebook(){

    }

    public void findByIdNotebook(){

    }


}
