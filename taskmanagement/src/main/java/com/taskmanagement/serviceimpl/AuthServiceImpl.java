package com.taskmanagement.serviceimpl;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.taskmanagement.constants.Constants;
import com.taskmanagement.dto.AuthRequest;
import com.taskmanagement.dto.AuthResponse;
import com.taskmanagement.dto.UserRequest;
import com.taskmanagement.entities.Role;
import com.taskmanagement.entities.User;
import com.taskmanagement.entities.VerificationToken;
import com.taskmanagement.enums.RoleName;
import com.taskmanagement.enums.UserStatus;
import com.taskmanagement.exceptionhandler.CustomException;
import com.taskmanagement.mail.MailService;
import com.taskmanagement.repositories.RoleRepository;
import com.taskmanagement.repositories.UserRepository;
import com.taskmanagement.repositories.VerificationTokenRepository;
import com.taskmanagement.security.jwt.JwtTokenProvider;
import com.taskmanagement.service.AuthService;
import com.taskmanagement.service.VerificationTokenService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Service
@Slf4j(topic = "AuthService")
@Transactional(rollbackOn = Exception.class)
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final VerificationTokenRepository tokenRepository;
    private final VerificationTokenService verificationTokenService;
    private final MailService mailService;

    @Override
    public AuthResponse auth(AuthRequest login) {
        try {
            log.info("Start Authen: {}", login);

            // Get username or email
            String userRequest = login.getUsernameOrEmail();

            // Get fullname
            String name = userRequest.contains("@") ?
                    userRepository.getFullnameByEmail(userRequest) :
                    userRepository.getFullnameByUsername(userRequest);

            Authentication authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(userRequest, login.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Generate Access Token
            String accessToken = jwtTokenProvider.generateAccessToken(authentication, name);
            // Generate Refresh Token
            String refreshToken = jwtTokenProvider.generateRefreshToken(authentication, name);

            return new AuthResponse(accessToken, refreshToken, Constants.BEARER_TOKEN);
        } catch (BadCredentialsException ex) {
            log.warn("Invalid login attempt for: {}", login.getUsernameOrEmail());
            throw new CustomException("Username or password is incorrect", HttpStatus.UNAUTHORIZED);
        } catch (AuthenticationException ex) {
            log.error("Authentication failed", ex);
            throw new CustomException("Authentication failed", HttpStatus.UNAUTHORIZED);
        }
    }

    @Override
    public void register(UserRequest userRequest) {
        try {
            log.info("START REGISTER USER");
            if (userRepository.existsByUsername(userRequest.getUsername())
                    || userRepository.existsByEmail(userRequest.getEmail())) {
                throw new IllegalArgumentException("Username hoặc Email đã tồn tại");
            }

            // Create user
            User user = new User();
            user.setId(userRepository.getMaxId() + 1);
            user.setName(userRequest.getName());
            user.setUsername(userRequest.getUsername());
            user.setEmail(userRequest.getEmail());
            user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
            user.setCreatedAt (LocalDateTime.now());
            user.setCreatedAt (null);
            user.setStatus(UserStatus.INACTIVE);
            // Get Role Id
            int roleUserId = roleRepository.findAll()
                    .stream()
                    .filter(role -> RoleName.ROLE_USER.name().equals(role.getName()))
                    .map(Role::getId)
                    .findFirst()
                    .orElse(0);
            user.setRoleId(roleUserId);
            
            userRepository.save(user);

            // tạo token verify
            String token = UUID.randomUUID().toString();
            verificationTokenService.saveTokenRegister(user, token);

            // gửi email
            mailService.sendVerificationEmail(user, token);
        } catch (Exception e) {
            log.error("Has error when register user: {} ", e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public boolean checkValidToken(String token) {
        try {
            log.info("Start check valid token:");
            VerificationToken verificationToken = tokenRepository.findByToken(token);

            if (verificationToken == null) {
                log.warn("verificationToken is null!");
                return false;
            }

            if (!isWithin30Minutes(verificationToken)) {
                log.warn("verificationToken is expiry!");
                return false;
            }

            User user = userRepository.findUserById(verificationToken.getId());
            user.setStatus(UserStatus.ACTIVE);
            userRepository.save(user);

            // Delete
            tokenRepository.delete(verificationToken);

            return true;
        } catch (Exception e) {
            log.error("Error check valid token: {}", e.getMessage(), e);
            throw e;
        }
    }

    /**
     * 
     * @param verificationToken
     * @return
     */
    private boolean isWithin30Minutes(VerificationToken verificationToken) {
        LocalDateTime now = LocalDateTime.now();
        Duration duration = Duration.between(verificationToken.getCreatedAt(), verificationToken.getExpiryDate());

        if (duration.toMinutes() <= 30) {
            // Kiểm tra xem createdAt có không phải là sau thời điểm hiện tại
            if (!verificationToken.getCreatedAt().isAfter(now)) {
                // Kiểm tra xem expiryDate có không phải là trước thời điểm hiện tại
                if (!verificationToken.getExpiryDate().isBefore(now)) {
                    return true;
                }
            }
        }
        return false;
    }

}
