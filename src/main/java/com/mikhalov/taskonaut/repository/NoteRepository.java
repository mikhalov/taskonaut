package com.mikhalov.taskonaut.repository;

import com.mikhalov.taskonaut.model.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoteRepository extends JpaRepository<Note, String> {

    List<Note> findAllByOrderByLastModifiedDateDesc();
    List<Note> findByLabelNameOrderByLastModifiedDateDesc(String labelName);

}
