package com.taskmanagement.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.taskmanagement.entities.VerificationToken;

@Repository
public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Integer> {

    @Query(value = "SELECT * FROM tbl_verification_token WHERE token = ?1", nativeQuery = true)
    VerificationToken findByToken(String token);
    
    @Query(value = "SELECT user_id FROM tbl_verification_token WHERE token = ?1", nativeQuery = true)
    Integer findUserIdByToken(String token);
}
