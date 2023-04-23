package com.mikhalov.taskonaut.service;

import com.mikhalov.taskonaut.dto.SignInDTO;
import com.mikhalov.taskonaut.exception.TelegramAccountAlreadyConnected;
import com.mikhalov.taskonaut.mapper.UserMapper;
import com.mikhalov.taskonaut.model.User;
import com.mikhalov.taskonaut.model.enums.UserRole;
import com.mikhalov.taskonaut.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public void createUser(SignInDTO signInDTO) {
        User user = userMapper.toUser(signInDTO);
        user.setRole(UserRole.USER);
        String encoded = new BCryptPasswordEncoder().encode(user.getPassword());
        user.setPassword(encoded);
        userRepository.save(user);
        log.info("user created '{}'", user.getEmail());
    }

    public User getCurrentUser() {
        String email = getCurrentUserUsername();
        return getUserByEmail(email);
    }

    public User getUserByEmail(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
    }

    public String getCurrentUserUsername() {
        Authentication authentication = Optional.ofNullable(SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                )
                .filter(Authentication::isAuthenticated)
                .orElseThrow(() -> new AuthenticationCredentialsNotFoundException(
                        "There is no Authentication object in the SecurityContext.")
                );

        return authentication.getName();
    }

    public void setTelegramChatIdByUserId(long chatId, String userId) throws TelegramAccountAlreadyConnected {
        boolean isEmpty = userRepository.findByTelegramChatId(chatId).isEmpty();
        if (isEmpty) {
            User user = userRepository.findById(userId).orElseThrow(EntityNotFoundException::new);
            user.setTelegramChatId(chatId);

            userRepository.save(user);
        } else {
            log.error("Telegram {} already connected", chatId);
            throw new TelegramAccountAlreadyConnected();
        }
    }

    public Optional<Long> getCurrentUserTelegramChatId() {
        return Optional.ofNullable(
                getCurrentUser().getTelegramChatId()
        );
    }

    @Cacheable(value = "telegramChatIds", key = "#chatId", unless = "#result == false")
    public boolean isChatIdRegisteredByUser(long chatId) {
        return userRepository.existsByTelegramChatId(chatId);
    }

    @CacheEvict(value = "telegramChatIds", key = "#chatId")
    public void removeChatIdFromUser(Long chatId) {
        User user = userRepository.findByTelegramChatId(chatId)
                .orElseThrow(EntityNotFoundException::new);
        user.removeChatId();

        update(user);
    }

    private void update(User user) {
        userRepository.save(user);
    }
}
