package com.mikhalov.taskonaut.repository;

import com.mikhalov.taskonaut.model.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoteRepository extends JpaRepository<Note, String> {

    @Query("""
            SELECT n
            FROM Note n
            WHERE (LOWER(n.title) LIKE LOWER(CONCAT('%', :keyword, '%')))
            OR (LOWER(n.content) LIKE LOWER(CONCAT('%', :keyword, '%')))
            OR (LOWER(n.label.name) LIKE LOWER(CONCAT('%', :keyword, '%')))
            AND LOWER(n.user.email) = LOWER(:userEmail)
            ORDER BY n.id DESC""")
    List<Note> findByTitleOrContentContainingAndUserEmailOrderByDesc(@Param("keyword") String keyword, @Param("userEmail") String userEmail);
    List<Note> findAllByUserEmailOrderByLastModifiedDateDesc(String email);


}
