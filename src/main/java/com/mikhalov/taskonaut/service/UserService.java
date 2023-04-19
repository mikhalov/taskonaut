package com.mikhalov.taskonaut.service;

import com.mikhalov.taskonaut.dto.UserRegistrationDTO;
import com.mikhalov.taskonaut.mapper.UserMapper;
import com.mikhalov.taskonaut.model.User;
import com.mikhalov.taskonaut.model.enums.UserRole;
import com.mikhalov.taskonaut.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public void createUser(UserRegistrationDTO userRegistrationDTO) {
        User user = userMapper.toUser(userRegistrationDTO);
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
        return SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();
    }

    public void setCurrentUserTelegramChatId(long chatId) {
        User user = getCurrentUser();
        user.setTelegramChatId(chatId);

        userRepository.save(user);
    }

    public Long getCurrentUserTelegramChatId() {
        return getCurrentUser().getTelegramChatId();
    }
}
