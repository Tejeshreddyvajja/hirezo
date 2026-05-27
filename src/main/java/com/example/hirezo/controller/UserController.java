package com.example.hirezo.controller;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.access.prepost.PreAuthorize;

import com.example.hirezo.dto.UserDto;
import com.example.hirezo.dto.UserResponseDto;
import com.example.hirezo.entity.UserEntity;
import com.example.hirezo.service.UserService;

import org.modelmapper.ModelMapper;
import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final ModelMapper modelMapper;

    @GetMapping("/hello")
    public String hello() {
        return "Hello, World!";
    }

    @PostMapping
    public ResponseEntity<UserResponseDto> createUser(@Valid @RequestBody UserDto userDto) {
        UserEntity createdUser = userService.createUser(userDto);
        UserResponseDto createdUserDto = modelMapper.map(createdUser, UserResponseDto.class);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUserDto);
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<UserResponseDto>> getAllUsers() {
        List<UserEntity> users = userService.getAllUsers();
        List<UserResponseDto> userDtos = users.stream()
                .map(user -> modelMapper.map(user, UserResponseDto.class))
                .toList();
        return ResponseEntity.status(HttpStatus.OK).body(userDtos);
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable Long id) {
        UserEntity user = userService.getUserById(id);
        UserResponseDto userDtoId = modelMapper.map(user, UserResponseDto.class);
        return ResponseEntity.status(HttpStatus.OK).body(userDtoId);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    // @updateMapping("/users/{id}")
    // public String updateMethod(@RequestParam String param) {
    //     return new String();
    // }


}
