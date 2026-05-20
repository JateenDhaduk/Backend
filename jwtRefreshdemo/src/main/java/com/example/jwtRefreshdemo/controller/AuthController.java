package com.example.jwtRefreshdemo.controller;

import com.example.jwtRefreshdemo.dto.*;
import com.example.jwtRefreshdemo.service.UserService;
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
    private final UserService userService;

    @PostMapping("/register")
    @Operation(summary = "create a new user" , description = "Endpoint for user register")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User registered successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "409", description = "Email or username already exists")
    })
    public ResponseEntity<ApiUserResponse<UserRegisterResponse>> registerUser(@Valid @RequestBody UserRegisterRequest request){
        UserRegisterResponse response = userService.registerUser(request);
        return ResponseEntity.ok(ApiUserResponse.success(200,"Account created Successfully",response));
    }

    @PostMapping("/login")
    @Operation(summary = "Login a user", description = "Endpoint to login a user with email and password.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User logged in successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid credentials")
    })
    public ResponseEntity<ApiUserResponse<UserLoginResponse>> loginUser(@Valid @RequestBody UserLoginRequest request) {
        // Placeholder for user login logic
        UserLoginResponse authResponse = userService.loginUser(request);
        return ResponseEntity.ok(ApiUserResponse.success("Login successful", authResponse));
    }

}
