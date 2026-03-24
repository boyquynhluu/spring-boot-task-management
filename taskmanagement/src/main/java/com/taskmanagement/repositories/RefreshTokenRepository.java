package com.taskmanagement.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.taskmanagement.entities.RefreshToken;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Integer> {

    @Query(value = "SELECT MAX(id) FROM tbl_refresh_token", nativeQuery = true)
    Long getMaxId();

    @Query(value = "SELECT * FROM tbl_refresh_token WHERE id=?1", nativeQuery = true)
    RefreshToken findByUserId(Long id);
}
