package com.example.demo.service;

import com.example.demo.dto.UserResponse;

public interface UserService {
    UserResponse getCurrentUser(Long userId);
    UserResponse getUserById(Long id);
}
