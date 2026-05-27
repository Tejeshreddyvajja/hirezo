package com.example.hirezo.service;

import com.example.hirezo.dto.AuthResponseDto;
import com.example.hirezo.dto.LoginRequestDto;
import com.example.hirezo.dto.UserDto;

public interface AuthService {

    AuthResponseDto login(LoginRequestDto request);

    AuthResponseDto signup(UserDto request);
}