package com.taskmanagement.entities;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "tbl_verification_token")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class VerificationToken {

    @Id
    int id;

    @Column(name = "token", unique = true)
    String token;

    @Column(name = "created_at", nullable = false)
    LocalDateTime createdAt;

    @Column(name = "expiry_date", nullable = false)
    LocalDateTime expiryDate;

    @Column(name = "user_id")
    int userId;
}
