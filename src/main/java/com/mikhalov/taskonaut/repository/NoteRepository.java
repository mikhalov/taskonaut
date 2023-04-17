package com.mikhalov.taskonaut.repository;

import com.mikhalov.taskonaut.model.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface NoteRepository extends JpaRepository<Note, String>, JpaSpecificationExecutor<Note> {

}
