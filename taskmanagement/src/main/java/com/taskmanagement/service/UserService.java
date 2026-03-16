package com.taskmanagement.service;

public interface UserService {

    public void registerRefreshToken(String usernameOrPassword, String token);

    public void deleteByRefreshToken(String username);
}
