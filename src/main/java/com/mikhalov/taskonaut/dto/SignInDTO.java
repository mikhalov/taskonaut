package com.mikhalov.taskonaut.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.*;

@Getter
@Setter
public class SignInDTO {

    @Email(message = "Please provide a valid email address")
    @NotBlank(message = "Email is required")
    @NotNull
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters long")
    @NotNull
    private String password;
}
