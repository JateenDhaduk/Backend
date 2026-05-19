package com.example.jwtRefreshdemo.dto;

import lombok.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApiUserResponse<T> {
    private int statusCode;
    private String message;
    private boolean success;
    @Builder.Default
    private LocalDateTime localDateTime = LocalDateTime.now();
    private T data;

    public static  <T> ApiUserResponse<T> success(String message){
        return ApiUserResponse.<T>builder()
                .success(true)
                .message(message)
                .statusCode(200)
                .build();
    }
    public static <T> ApiUserResponse<T> success(String message , T data){
        return ApiUserResponse.<T>builder()
                .success(true)
                .statusCode(200)
                .data(data)
                .message(message)
                .build();
    }
    public static <T> ApiUserResponse<T> success(int statusCode, String message, T data) {
        return ApiUserResponse.<T>builder()
                .success(true)
                .statusCode(statusCode)
                .message(message)
                .data(data)
                .build();
    }
}
