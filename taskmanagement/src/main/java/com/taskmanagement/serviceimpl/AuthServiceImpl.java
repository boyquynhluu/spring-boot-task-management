package com.taskmanagement.serviceimpl;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.ResourceClosedException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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

import jakarta.persistence.EntityNotFoundException;
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
            String usernameOrEmail = login.getUsernameOrEmail();

            User user = null;
            if (usernameOrEmail.contains("@")) {
                user = userRepository.findByEmail(usernameOrEmail).orElseThrow(
                        () -> new UsernameNotFoundException("User not found with email: " + usernameOrEmail));
            } else {
                user = userRepository.findByUsername(usernameOrEmail)
                        .orElseThrow(() -> new UsernameNotFoundException("User not exist by Username or Email"));
            }

            // Check Status
            String status = user.getStatus().name();
            if(status.equals(UserStatus.INACTIVE.name())) {
                throw new DisabledException("Tài khoản chưa được kích hoạt!");
            }
            if(status.equals(UserStatus.BANNED.name())) {
                throw new DisabledException("Tài Khoản Đã Bị Khóa!");
            }

            Authentication authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(usernameOrEmail, login.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Generate Access Token
            String accessToken = jwtTokenProvider.generateAccessToken(authentication, user);
            // Generate Refresh Token
            String refreshToken = jwtTokenProvider.generateRefreshToken(authentication, user);

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
            // Get Max ID
            Long maxId = userRepository.getMaxId();
            user.setId(maxId == null ? 1 : maxId + 1);
            user.setName(userRequest.getName());
            user.setUsername(userRequest.getUsername());
            user.setEmail(userRequest.getEmail());
            user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
            user.setCreatedAt(LocalDateTime.now());
            user.setUpdatedAt(null);
            user.setStatus(UserStatus.INACTIVE);
            // Get Role Id
            Long roleUserId = roleRepository.findAll()
                    .stream()
                    .filter(role -> RoleName.ROLE_USER.name().equals(role.getRoleName().name()))
                    .map(Role::getId)
                    .findFirst()
                    .orElse(null);
            user.setRoleId(roleUserId);

            userRepository.save(user);

            // Create token verify
            String token = UUID.randomUUID().toString();
            verificationTokenService.saveTokenRegister(user, token);

            // Send email
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
                throw new EntityNotFoundException("Token Incorrect!");
            }

            if (!isWithin30Minutes(verificationToken)) {
                log.warn("verificationToken is expiry!");
                throw new IllegalStateException("Account verification expired!");
            }

            User user = userRepository.findUserById(verificationToken.getId());
            user.setStatus(UserStatus.ACTIVE);
            user.setUpdatedAt(LocalDateTime.now());
            userRepository.save(user);

            // Delete verificationToken
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
        return now.isAfter(verificationToken.getCreatedAt())
                && now.isBefore(verificationToken.getExpirationAt());
    }

}
