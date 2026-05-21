package com.example.jwtRefreshdemo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class LoginResult {
    private UserLoginResponse userLoginResponse;
    private String rawRefreshToken;
}
