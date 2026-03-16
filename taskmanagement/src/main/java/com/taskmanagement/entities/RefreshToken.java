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
@Table(name = "tbl_refresh_token")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RefreshToken {

    @Id
    int id;

    @Column(name = "refresh_token")
    String token;

    @Column(name = "created_at")
    LocalDateTime createdAt;

    @Column(name = "expiration_at")
    LocalDateTime expirationAt;

    @Column(name = "user_id")
    int userId;
}