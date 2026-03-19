package com.taskmanagement.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.taskmanagement.entities.AuditLog;

@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {

    @Query(value = "SELECT MAX(id) FROM tbl_audit_log", nativeQuery = true)
    Long getMaxId();
}
