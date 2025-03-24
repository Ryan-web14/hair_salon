package com.sni.hairsalon.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.sni.hairsalon.model.UserSession;

@Repository
public interface UserSessionRepository extends JpaRepository<UserSession,Long>{
    UserSession findUserSessionById(long id);
    UserSession findUserSessionByIpAddress(String ipAddress);
    List<UserSession> findUserSessionByUserId(long userId);
    
@Query("SELECT s FROM UserSession s WHERE s.user.id = :userId AND s.ipAddress = :ipAddress AND s.isActive = true")
Optional<UserSession> findByUserIdAndIpAddressAndActive(@Param("userId") Long userId, @Param("ipAddress") String ipAddress);

 // Find user sessions by user agent
@Query("SELECT s FROM UserSession s WHERE s.userAgent = :userAgent")
List<UserSession> findUserByUserAgent(@Param("userAgent") String userAgent);

// Find an active session by token
@Query("SELECT s FROM UserSession s WHERE s.token = :token AND s.isActive = true")
Optional<UserSession> findByTokenAndIsActiveTrue(@Param("token") String token);

// Find any session by token (active or not)
@Query("SELECT s FROM UserSession s WHERE s.token = :token")
Optional<UserSession> findByToken(@Param("token") String token);

// Find all active sessions
@Query("SELECT s FROM UserSession s WHERE s.isActive = true")
List<UserSession> findByIsActiveTrue();

// Find active sessions for a specific user
@Query("SELECT s FROM UserSession s WHERE s.user.id = :userId AND s.isActive = true")
List<UserSession> findByUser_IdAndIsActiveTrue(@Param("userId") Long userId);

}
