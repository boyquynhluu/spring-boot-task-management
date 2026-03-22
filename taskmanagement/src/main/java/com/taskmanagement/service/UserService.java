package com.taskmanagement.service;

import com.taskmanagement.dto.UserResponse;

public interface UserService {

    public void registerRefreshToken(String usernameOrPassword, String token);

    public void deleteByRefreshToken(String username);

    public UserResponse getUser(String usernameOrEmail);
}
