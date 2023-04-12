package com.mikhalov.taskonaut.controller;

import com.mikhalov.taskonaut.dto.UserRegistrationDTO;
import com.mikhalov.taskonaut.jwt.JwtUtil;
import com.mikhalov.taskonaut.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Slf4j
@Controller
@RequestMapping("/login")
@RequiredArgsConstructor
public class LoginController {

    private static final String LOGIN_VIEW = "/login";
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtUtil jwtUtil;

    @GetMapping()
    public ModelAndView loginPage(ModelAndView modelAndView) {
        log.info("login page");
        modelAndView.addObject(new UserRegistrationDTO());
        modelAndView.setViewName(LOGIN_VIEW);
        return modelAndView;
    }

    @PostMapping("/signup")
    public ModelAndView signup(
            @Valid @ModelAttribute UserRegistrationDTO userRegistrationDTO,
            BindingResult bindingResult,
            ModelAndView modelAndView,
            HttpServletResponse response) {
        log.info("signup email '{}'", userRegistrationDTO.getEmail());
        modelAndView.setViewName(LOGIN_VIEW);

        if (bindingResult.hasErrors()) {
            log.info("has errors");

            return modelAndView;
        }

        try {
            userService.createUser(userRegistrationDTO);

            return authentication(
                    userRegistrationDTO,
                    response
            );
        } catch (DataIntegrityViolationException e) {
            log.info("Registration failed due to duplicate email.");
            bindingResult.rejectValue(
                    "email",
                    "error.userRegistrationDTO",
                    "An account with this email already exists."
            );
        } catch (Exception e) {
            log.error("Registration failed due to an unexpected error.", e);
            modelAndView.addObject(
                    "generalError",
                    "An unexpected error occurred. Please try again."
            );
        }

        return modelAndView;
    }

    @PostMapping("/auth")
    public ModelAndView authentication(
            @ModelAttribute UserRegistrationDTO userRegistrationDTO,
            HttpServletResponse response) {
        log.info("authentication");
        ModelAndView modelAndView = new ModelAndView();
        try {
            Authentication authRequest = new UsernamePasswordAuthenticationToken(
                    userRegistrationDTO.getEmail(),
                    userRegistrationDTO.getPassword()
            );
            Authentication authResult = authenticationManager.authenticate(authRequest);
            UserDetails userDetails = (UserDetails) authResult.getPrincipal();

            String jwtToken = jwtUtil.generateToken(userDetails);
            Cookie authCookie = jwtUtil.createAuthCookie(jwtToken);

            response.addCookie(authCookie);
            modelAndView.setViewName("redirect:/notes");

            return modelAndView;
        } catch (AuthenticationException e) {
            modelAndView.addObject(
                    "generalError",
                    "Wrong credentials. Please try again."
            );
            modelAndView.setViewName(LOGIN_VIEW);

            return modelAndView;
        }
    }

}