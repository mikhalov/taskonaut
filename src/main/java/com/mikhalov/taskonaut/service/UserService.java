package com.mikhalov.taskonaut.service;

import com.mikhalov.taskonaut.model.User;
import com.mikhalov.taskonaut.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public void createUser(User user) {
        String encoded = String.format("{bcrypt}%s", new BCryptPasswordEncoder().encode(user.getPassword()));
        user.setPassword(encoded);
        userRepository.save(user);
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

}
