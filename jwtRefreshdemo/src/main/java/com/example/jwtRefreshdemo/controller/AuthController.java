package com.example.jwtRefreshdemo.controller;

import com.example.jwtRefreshdemo.dto.ApiUserResponse;
import com.example.jwtRefreshdemo.dto.UserRegisterRequest;
import com.example.jwtRefreshdemo.dto.UserRegisterResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth/user")
@RequiredArgsConstructor
public class AuthController {
    private final UserServics userServics;

    @PostMapping("/register")
    @Operation(summary = "create a new user" , description = "Endpoint for user register")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User registered successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "409", description = "Email or username already exists")
    })
    public ResponseEntity<ApiUserResponse<UserRegisterResponse>> registerUser(@Valid @RequestBody UserRegisterRequest request){}
        UserRegisterResponse response = userServics.registerUser(request);
         return ResponseEntity.ok(ApiUserResponse.success(201, "Account created successfully", response));
}
