package com.example.hirezo.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.example.hirezo.dto.AuthResponseDto;
import com.example.hirezo.dto.LoginRequestDto;
import com.example.hirezo.dto.UserDto;
import com.example.hirezo.dto.UserResponseDto;
import com.example.hirezo.entity.UserEntity;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @Override
    public AuthResponseDto login(LoginRequestDto request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        UserEntity user = (UserEntity) authentication.getPrincipal();
        String token = jwtService.generateToken(user);
        return new AuthResponseDto(token, "Bearer", toUserResponseDto(user));
    }

    @Override
    public AuthResponseDto signup(UserDto request) {
        UserEntity user = userService.createUser(request);
        String token = jwtService.generateToken(user);
        return new AuthResponseDto(token, "Bearer", toUserResponseDto(user));
    }

    private UserResponseDto toUserResponseDto(UserEntity user) {
        return new UserResponseDto(user.getId(), user.getName(), user.getEmail(), user.getRole());
    }
}