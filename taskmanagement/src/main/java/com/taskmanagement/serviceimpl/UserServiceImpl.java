package com.taskmanagement.serviceimpl;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.taskmanagement.constants.Constants;
import com.taskmanagement.dto.UserRequest;
import com.taskmanagement.entities.RefreshToken;
import com.taskmanagement.entities.User;
import com.taskmanagement.repositories.RefreshTokenRepository;
import com.taskmanagement.repositories.UserRepository;
import com.taskmanagement.service.UserService;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j(topic = "USER SERVICE")
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class UserServiceImpl implements UserService {

    @Value(Constants.API_JWT_REFRESH_EXPIRATION_MILLISECONDS)
    private long jwtRefreshExpirationDate;

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void registerRefreshToken(String usernameOrPassword, String token) {
        try {
            User user;
            if (usernameOrPassword.contains("@")) {
                user = userRepository.findByEmail(usernameOrPassword).orElseThrow(
                        () -> new UsernameNotFoundException("User not found with email: " + usernameOrPassword));
            } else {
                user = userRepository.findByUsername(usernameOrPassword)
                        .orElseThrow(() -> new UsernameNotFoundException("User not exist by Username or Email"));
            }

            RefreshToken refreshToken = new RefreshToken();
            refreshToken.setToken(token);
            refreshToken.setCreatedAt(LocalDateTime.now());
            refreshToken.setExpirationAt(formatLocalDateTime());
            refreshToken.setUserId(user.getId());

            refreshTokenRepository.save(refreshToken);
        } catch (Exception e) {
            log.error("Has error when register refresh token: {}", e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public void deleteByRefreshToken(String username) {
        User user;
        if (username.contains("@")) {
            user = userRepository.findByEmail(username)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + username));
        } else {
            user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException("User not exist by Username or Email"));
        }

        RefreshToken refreshToken = refreshTokenRepository.findRefreshTokenById(user.getId());
        if (!Objects.isNull(refreshToken)) {
            refreshTokenRepository.delete(refreshToken);
        }
    }

    /**
     * Convert LocalDateTime
     * 
     * @return yyyy-MM-dd HH:mm:ss formatted LocalDateTime
     */
    private LocalDateTime formatLocalDateTime() {
        try {
            Date currentDate = new Date();
            Date expireDate = new Date(currentDate.getTime() + jwtRefreshExpirationDate);
            return expireDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        } catch (Exception e) {
            log.error("Parse LocalDateTime has error: {}", e.getMessage(), e);
            throw e;
        }
    }
}