package com.mikhalov.taskonaut.controller;

import com.mikhalov.taskonaut.dto.SignInDTO;
import com.mikhalov.taskonaut.service.AuthenticationService;
import com.mikhalov.taskonaut.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.AuthenticationException;
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
public class AuthenticationController {

    private static final String LOGIN_VIEW = "/login";
    private final AuthenticationService authenticationService;
    private final UserService userService;

    @GetMapping()
    public ModelAndView loginPage(ModelAndView modelAndView) {
        modelAndView.addObject(new SignInDTO());
        modelAndView.setViewName(LOGIN_VIEW);
        return modelAndView;
    }

    @PostMapping("/signup")
    public ModelAndView signup(
            @Valid @ModelAttribute SignInDTO signInDTO,
            BindingResult bindingResult,
            ModelAndView modelAndView,
            HttpServletResponse response) {
        log.info("signup email '{}'", signInDTO.getEmail());
        modelAndView.setViewName(LOGIN_VIEW);

        if (bindingResult.hasErrors()) {
            log.debug("has errors");

            return modelAndView;
        }

        try {
            userService.createUser(signInDTO);

            return authentication(signInDTO, response);
        } catch (DataIntegrityViolationException e) {
            log.error("Registration failed due to duplicate email: '{}'", signInDTO.getEmail());
            bindingResult.rejectValue(
                    "email",
                    "error.userRegistrationDTO",
                    "An account with this email already exists."
            );
        } catch (Exception e) {
            log.error("Registration failed due to an unexpected error. User '{}'", signInDTO.getEmail(), e);
            modelAndView.addObject(
                    "generalError",
                    "An unexpected error occurred. Please try again."
            );
        }

        return modelAndView;
    }

    @PostMapping("/auth")
    public ModelAndView authentication(
            @ModelAttribute SignInDTO signInDTO,
            HttpServletResponse response) {
        log.trace("authentication for user '{}'", signInDTO.getEmail());
        ModelAndView modelAndView = new ModelAndView();
        try {
            Cookie authCookie = authenticationService
                    .getAuthCookiesIfCredentialsValid(signInDTO);
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