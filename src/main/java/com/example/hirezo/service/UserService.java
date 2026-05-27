package com.example.hirezo.service;

import com.example.hirezo.dto.UserDto;
import com.example.hirezo.entity.UserEntity;
import java.util.*;

public interface UserService {

    UserEntity createUser(UserDto userDto);

    List<UserEntity> getAllUsers();

    UserEntity getUserById(Long id);

    void deleteUser(Long id);
}
