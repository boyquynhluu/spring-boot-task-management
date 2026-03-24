package com.taskmanagement.service;

import com.taskmanagement.dto.UserResponse;
import com.taskmanagement.entities.User;

public interface UserService {

    public void registerRefreshToken(String usernameOrEmail, String refreshToken);

    public void deleteByRefreshToken(String username);

    public UserResponse getUser(String email);

    public User getUserByEmail(String email);
}
