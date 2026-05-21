package com.example.jwtRefreshdemo.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserLoginResponse {
    private String userName;
    private String email;
    private String role;
    private String accessToken;
    private String tokenType;
    private long expiresIn;
}
