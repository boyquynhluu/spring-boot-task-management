package com.taskmanagement.service;

import com.taskmanagement.entities.User;

public interface VerificationTokenService {

    void saveTokenRegister(User use, String token);
}
