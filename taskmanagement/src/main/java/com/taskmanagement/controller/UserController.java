package com.taskmanagement.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cloudinary.utils.StringUtils;
import com.taskmanagement.security.jwt.JwtTokenProvider;
import com.taskmanagement.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(value = "/api/user")
@Slf4j(topic = "USER CONTROLLER")
@RequiredArgsConstructor
public class UserController {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;

    @GetMapping("/me")
    public Map<String, Object> getUser(HttpServletRequest request, HttpServletResponse response) {
        log.info("GET USER");
        // Get refresh token from cookie
        String accessToken = jwtTokenProvider.getAccessTokenFromCookie(request);
        if (StringUtils.isEmpty(accessToken)) {
            return Map.of("status", HttpStatus.UNAUTHORIZED, "message", "User Not Login");
        }
        // validate refreshToken
        jwtTokenProvider.validateToken(accessToken);

        return Map.of("user", userService.getUser(jwtTokenProvider.getUsername(accessToken)));
    }

}
