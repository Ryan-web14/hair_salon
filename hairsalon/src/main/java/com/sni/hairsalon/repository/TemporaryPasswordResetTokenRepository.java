package com.sni.hairsalon.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.sni.hairsalon.model.TemporaryPasswordResetToken;

@Repository
public interface TemporaryPasswordResetTokenRepository extends JpaRepository<TemporaryPasswordResetToken,Long> {
    
     
    Optional<TemporaryPasswordResetToken> findByToken(String token);
    Optional<TemporaryPasswordResetToken> findByEmail(String email);
    
    @Modifying
    @Query("delete from TemporaryPasswordResetToken t where t.email = :email")
    void deleteByEmail(String email);

    @Query("select t.email from TemporaryPasswordResetToken t where t.token = :token")
    String findEmailByToken(String token);
}
