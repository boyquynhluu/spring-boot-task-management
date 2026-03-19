package com.taskmanagement.serviceimpl;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.taskmanagement.entities.AuditLog;
import com.taskmanagement.enums.AuditAction;
import com.taskmanagement.repositories.AuditLogRepository;
import com.taskmanagement.service.AuditLogService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j(topic = "AUDIT LOG")
@RequiredArgsConstructor
public class AuditLogServiceImpl implements AuditLogService {

    private final AuditLogRepository auditLogRepository;

    @Override
    public void register(AuditAction action, String entityType, Long entityId, Long userId) {
        try {
            log.info("START REGISTER LOG");
            Long maxId = auditLogRepository.getMaxId();
            AuditLog auditLog = new AuditLog();
            auditLog.setId(maxId == null ? 1 : maxId + 1);
            auditLog.setAction(action);
            auditLog.setEntityType(entityType);
            auditLog.setEntityId(entityId);
            auditLog.setCreatedAt(LocalDateTime.now());
            auditLog.setUserId(userId);
            auditLogRepository.save(auditLog);
        } catch (Exception e) {
            log.error("Register Audit Log Has Error: {}", e.getMessage(), e);
            throw e;
        }
    }
}
