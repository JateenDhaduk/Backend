package com.example.jwtRefreshdemo.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRegisterResponse {
    private Long id;
    private String username;
    private String email;
}
