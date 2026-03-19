package com.taskmanagement.serviceimpl;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.taskmanagement.entities.User;
import com.taskmanagement.entities.VerificationToken;
import com.taskmanagement.repositories.VerificationTokenRepository;
import com.taskmanagement.service.VerificationTokenService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "VerificationTokenServiceImpl")
public class VerificationTokenServiceImpl implements VerificationTokenService {

    private final VerificationTokenRepository verificationTokenRepository;

    @Override
    public void saveTokenRegister(User user, String token) {

        try {
            log.info("Start Register Token");
            VerificationToken vt = new VerificationToken();
            // Get Max ID
            Long maxId = verificationTokenRepository.getMaxId();
            vt.setId(maxId == null ? 1 : maxId + 1);
            vt.setToken(token);
            vt.setUserId(user.getId());
            vt.setCreatedAt(LocalDateTime.now());
            vt.setExpirationAt(vt.getCreatedAt().plusMinutes(30));
            verificationTokenRepository.save(vt);
        } catch (Exception e) {
            log.error("Register Token Has Error: {}", e.getMessage(), e);
            throw e;
        }
    }
}
