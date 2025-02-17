package com.sni.hairsalon.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sni.hairsalon.model.User;
import com.sni.hairsalon.model.UserSession;

@Repository
public interface UserSessionRepository extends JpaRepository<UserSession,Long>{
    UserSession findUserSessionById(long id);
    UserSession findUserSessionByIpAddress(String ipAddress);
    List<UserSession> findUserSessionByUserId(long userId);
    List<UserSession> findUserByUserAgent(String userAgent);   
    Optional<UserSession> findByTokenAndIsActiveTrue(String token); 
    Optional<UserSession> findByToken(String token); 
    List<UserSession> findByIsActiveTrue();
    List<UserSession> findByUserAndIsActiveTrue(User user); 

}
