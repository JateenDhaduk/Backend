package com.example.jwtRefreshdemo.service;

import com.example.jwtRefreshdemo.dto.*;
import com.example.jwtRefreshdemo.entity.UserEntity;
import com.example.jwtRefreshdemo.repoitory.UserRepository;
import com.example.jwtRefreshdemo.security.JwtUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository repository;
    private final JwtUtils jwtUtils;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final RefreshTokenService refreshTokenService;
    public UserRegisterResponse registerUser(UserRegisterRequest request) {
        UserEntity tempUser = UserEntity.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .username(request.getUsername())
                .build();

        UserEntity saveUser = repository.save(tempUser);
        return mapToOne(saveUser);
    }

    private UserRegisterResponse mapToOne(UserEntity saveUser) {
        return UserRegisterResponse.builder()
                .id(saveUser.getId())
                .username(saveUser.getUsername())
                .email(saveUser.getEmail())
                .build();
    }

    public LoginResult loginUser(UserLoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                request.getUsername(),request.getPassword()
        ));
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        UserEntity user  = repository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found " + request.getUsername()));

        String accessToken = jwtUtils.generateAccessToken(userDetails);
        String refreshToken = refreshTokenService.createRefreshToken(user);

        UserLoginResponse loginResponse =  UserLoginResponse.builder()
                .userName(user.getUsername())
                .accessToken(accessToken)
                .role(user.getRole().toString())
                .email(user.getEmail())
                .tokenType("Bearer")
                .expiresIn(jwtUtils.getJwtExpirationMils()/1000)
                .build();

        return new LoginResult(loginResponse,refreshToken);
    }
}
