package com.example.jwtRefreshdemo.dto;

import lombok.Getter;

@Getter
public class UserLoginRequest {
    private String username;
    private String password;
}
