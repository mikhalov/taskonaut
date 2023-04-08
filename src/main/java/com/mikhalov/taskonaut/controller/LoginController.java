package com.mikhalov.taskonaut.controller;

import com.mikhalov.taskonaut.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.time.Duration;
import java.time.Instant;

@Slf4j
@Controller
@RequestMapping("/login")
@RequiredArgsConstructor
public class LoginController {

    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    @GetMapping()
    public ModelAndView loginPage() {
        log.info("login page");
        return new ModelAndView("/login");
    }

    @PostMapping("/auth")
    public ModelAndView authentication(
            @RequestParam("email") String email,
            @RequestParam("password") String password,
            HttpServletResponse response) {
        log.info("authentication");
        try {
            // Attempt to authenticate with provided email and password
            Authentication authRequest = new UsernamePasswordAuthenticationToken(email, password);
            Authentication authResult = authenticationManager.authenticate(authRequest);

            // Get the UserDetails object
            UserDetails userDetails = (UserDetails) authResult.getPrincipal();

            // Generate JWT token
            String jwtToken = jwtUtil.generateToken(userDetails);

            // Get the JWT token's expiration duration in seconds
            Instant expirationInstant = jwtUtil.getExpirationDateFromToken(jwtToken);
            long jwtExpirationDuration = Duration.between(Instant.now(), expirationInstant).getSeconds();

            // Create a new cookie with the JWT token's expiration time
            Cookie authCookie = new Cookie("Authorization", jwtToken);
            authCookie.setHttpOnly(true);
            authCookie.setMaxAge((int) jwtExpirationDuration);
            authCookie.setPath("/");

            // Add the cookie to the response
            response.addCookie(authCookie);

            return new ModelAndView("redirect:/notes"); // Redirect to the notes page
        } catch (AuthenticationException e) {
            // Handle authentication failure (e.g., show an error message or redirect to an error page)
            return new ModelAndView("login");
        }
    }
}