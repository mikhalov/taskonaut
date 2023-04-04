package com.mikhalov.taskonaut.service;

import com.mikhalov.taskonaut.model.User;
import com.mikhalov.taskonaut.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User getCurrentUser() {
        String email = getCurrentUserUsername();
        return getUserByEmail(email);
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    public String getCurrentUserUsername() {
        return SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();
    }

//    @Bean
//    public CommandLineRunner addUsers() {
//        return args -> {
//            User user1 = new User();
//            user1.setEmail("user@example.com");
//            user1.setPassword(new BCryptPasswordEncoder().encode("password"));
//            user1.setRole(UserRole.USER);
//
//            User user2 = new User();
//            user2.setEmail("admin@example.com");
//            user2.setPassword(new BCryptPasswordEncoder().encode("password"));
//            user2.setRole(UserRole.ADMIN);
//
//            userRepository.save(user1);
//            userRepository.save(user2);
//        };
//    }
}
