package com.taskmanagement.service;

import com.taskmanagement.enums.AuditAction;

public interface AuditLogService {

    void register(AuditAction action, String entityType, Long entityId, Long userId);
}
