package com.mikhalov.taskonaut.repository;

import com.mikhalov.taskonaut.model.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoteRepository extends JpaRepository<Note, String>, JpaSpecificationExecutor<Note> {
    @Query("""
            SELECT n
            FROM Note n JOIN FETCH n.label l JOIN l.user u
            WHERE u.email = :email
            AND l.name = :labelName""")
    List<Note> findByLabelNameAndUserEmail(String labelName, String email);

}
