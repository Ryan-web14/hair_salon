package com.sni.hairsalon.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.sni.hairsalon.model.PasswordResetToken;

@Repository
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
    
    Optional<PasswordResetToken> findByToken(String token);
    Optional<PasswordResetToken> findByEmail(String email);

    @Modifying
    @Query("delete from PasswordResetToken p where p.email = :email")
    void deleteByEmail(String email);

    @Query("select p.email from PasswordResetToken p where p.token = :token")
    String findEmailByToken(String token);
}
