package com.taskmanagement.entities;

import java.time.LocalDateTime;

import com.taskmanagement.enums.AuditAction;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tbl_audit_log")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AuditLog {

    @Id
    Long id;

    @Column(name = "action")
    @Enumerated(EnumType.STRING)
    AuditAction action;

    @Column(name = "entity_type")
    String entityType;

    @Column(name = "entity_id")
    Long entityId;

    @Column(name = "created_at")
    LocalDateTime createdAt;

    @Column(name = "user_id")
    Long userId;
}
