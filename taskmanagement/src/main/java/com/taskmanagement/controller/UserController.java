package com.taskmanagement.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cloudinary.utils.StringUtils;
import com.taskmanagement.exceptionhandler.CustomException;
import com.taskmanagement.security.jwt.JwtTokenProvider;
import com.taskmanagement.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
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
    public ResponseEntity<?> getUser(HttpServletRequest request) {
        log.info("GET USER");
        String accessToken = jwtTokenProvider.getAccessTokenFromCookie(request);

        if (StringUtils.isEmpty(accessToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        try {
            jwtTokenProvider.validateToken(accessToken);
        } catch (CustomException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        var user = userService.getUser(jwtTokenProvider.getEmailFromToken(accessToken));

        return ResponseEntity.ok(user); // 👈 trả thẳng user cho FE
    }

}
