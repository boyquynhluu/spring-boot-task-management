package com.taskmanagement.service;

import com.taskmanagement.dto.AuthRequest;
import com.taskmanagement.dto.AuthResponse;
import com.taskmanagement.dto.UserRequest;

public interface AuthService {
    AuthResponse auth(AuthRequest authRequest);

    void register(UserRequest userRequest);

    boolean checkValidToken(String token);
}
