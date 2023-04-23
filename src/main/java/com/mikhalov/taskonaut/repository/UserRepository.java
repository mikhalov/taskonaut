package com.mikhalov.taskonaut.repository;

import com.mikhalov.taskonaut.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByEmail(String email);

    Optional<User> findByTelegramChatId(long chatId);


    boolean existsByTelegramChatId(long telegramChatId);
}
