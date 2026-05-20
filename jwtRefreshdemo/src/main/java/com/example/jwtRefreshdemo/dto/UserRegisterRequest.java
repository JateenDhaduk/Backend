package com.example.jwtRefreshdemo.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NonNull;

@Getter
public class UserRegisterRequest {
    @NotBlank(message = "username is required")
    private String username;

    @Email
    @NotBlank(message = "email is required to create account")
    private String email;

    @Size(min = 6 , message = "password must at least 6 character or digit")
    @NotNull(message = "password is required ")
    private String password;
}
