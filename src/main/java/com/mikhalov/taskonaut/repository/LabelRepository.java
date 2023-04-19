package com.mikhalov.taskonaut.repository;

import com.mikhalov.taskonaut.model.Label;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LabelRepository extends JpaRepository<Label, String>, JpaSpecificationExecutor<Label> {
    Optional<Label> findByNameAndUserEmail(String name, String userEmail);

    List<Label> findAllByUserEmail(String userEmail);

    List<Label> findAllByUserTelegramChatIdOrderByName(long telegramChatId);

}
