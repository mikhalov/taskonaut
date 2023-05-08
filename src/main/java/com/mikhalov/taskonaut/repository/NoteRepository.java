package com.mikhalov.taskonaut.repository;

import com.mikhalov.taskonaut.model.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NoteRepository extends JpaRepository<Note, String>, JpaSpecificationExecutor<Note> {

    @Query("""
            SELECT DISTINCT n.title
            FROM Note n JOIN n.label l JOIN n.user u
            WHERE l.id = :labelId
            AND u.telegramChatId = :telegramChatId
            ORDER BY n.title""")
    List<String> findDistinctNoteTitlesByLabelIdAndTelegramChatId(
            @Param("labelId") String labelId,
            @Param("telegramChatId") Long telegramChatId
    );

    List<Note> findAllByTitleAndLabelIdAndUserTelegramChatIdOrderByLastModifiedDateDesc(
            String title, String labelId, Long chatId
    );

    Optional<Note> findByIdAndUserEmail(String id, String email);

    Optional<Note> findByIdAndUserTelegramChatId(String id, Long chatId);
}
