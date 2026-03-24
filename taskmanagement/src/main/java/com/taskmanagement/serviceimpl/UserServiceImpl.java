package com.taskmanagement.serviceimpl;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Objects;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.taskmanagement.constants.Constants;
import com.taskmanagement.dto.UserResponse;
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
    private final ModelMapper mapper;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void registerRefreshToken(String usernameOrEmail, String token) {

        // Get User
        User user = this.getUserByUsernameOrEmail(usernameOrEmail);

        RefreshToken refreshToken = new RefreshToken();
        // Get max id
        Long maxId = refreshTokenRepository.getMaxId();
        refreshToken.setId(maxId == null ? 1 : maxId + 1);
        refreshToken.setToken(token);
        refreshToken.setCreatedAt(LocalDateTime.now());
        refreshToken.setExpirationAt(formatLocalDateTime());
        refreshToken.setUserId(user.getId());

        refreshTokenRepository.save(refreshToken);
    }

    @Override
    public void deleteByRefreshToken(String usernameOrEmail) {
        log.info("DELETE REFRESH TOKEN");
        // Get User
        User user = this.getUserByUsernameOrEmail(usernameOrEmail);

        RefreshToken refreshToken = refreshTokenRepository.findByUserId(user.getId());
        if (!Objects.isNull(refreshToken)) {
            refreshTokenRepository.delete(refreshToken);
        }
    }


    @Override
    public User getUserByEmail(String email) {
        return userRepository.getUserByEmail(email);
    }

    @Override
    public UserResponse getUser(String usernameOrEmail) {

        // Get User
        User user = this.getUserByUsernameOrEmail(usernameOrEmail);

        return mapper.map(user, UserResponse.class);
    }

    /**
     * Get User by username or email
     * 
     * @param usernameOrEmail
     * @return
     */
    private User getUserByUsernameOrEmail(String usernameOrEmail) {
        if (usernameOrEmail.contains("@")) {
            return userRepository.findByEmail(usernameOrEmail).orElseThrow(
                    () -> new UsernameNotFoundException("User not found with email: " + usernameOrEmail));
        } else {
            return userRepository.findByUsername(usernameOrEmail)
                    .orElseThrow(() -> new UsernameNotFoundException("User not exist by Username or Email"));
        }
    }

    /**
     * Convert LocalDateTime
     * 
     * @return yyyy-MM-dd HH:mm:ss formatted LocalDateTime
     */
    private LocalDateTime formatLocalDateTime() {
        Date expireDate = new Date(new Date().getTime() + jwtRefreshExpirationDate);
        return expireDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }
}