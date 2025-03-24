package com.sni.hairsalon.service.serviceImpl;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.sni.hairsalon.dto.response.SessionResponseDTO;
import com.sni.hairsalon.dto.response.UserResponseDTO;
import com.sni.hairsalon.exception.ResourceNotFoundException;
import com.sni.hairsalon.mapper.SessionMapper;
import com.sni.hairsalon.model.User;
import com.sni.hairsalon.model.UserSession;
import com.sni.hairsalon.repository.UserRepository;
import com.sni.hairsalon.repository.UserSessionRepository;
import com.sni.hairsalon.security.Utils.JWTUtils;
import com.sni.hairsalon.service.SessionService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SessionServiceImpl implements SessionService {
    
   
    private final SessionMapper mapper;
    private final UserRepository userRepo;
    private final JWTUtils jwtUtils;
    private final UserSessionRepository sessionRepo;

    @Value("${sessionTimeout:2880}")
    private long sessionTimeout;


    @Override 
    public SessionResponseDTO createSession(HttpServletRequest request, UserResponseDTO user){
        User userFound = userRepo.findUserById(Long.parseLong(user.getId()))
        .orElseThrow(() -> new ResourceNotFoundException("User not found"));

            String clientIp = getClientIP(request);

        //check if there's a user session with the same ip address
        Optional<UserSession> existingSession = sessionRepo. findByUserIdAndIpAddressAndActive(userFound.getId(),  clientIp); 

        //if so return the session 
        if(existingSession.isPresent()){
            return mapper.toDto(existingSession.get());
        }
        List<UserSession> ActiveSessions = sessionRepo.findByUser_IdAndIsActiveTrue(userFound.getId());
        if(ActiveSessions.size() >= 3){
            UserSession oldSession =  ActiveSessions.stream()
            .min(Comparator.comparing(UserSession::getCreated_at))
            .orElse(null);
            if(oldSession != null){
                oldSession.setActive(false);
                sessionRepo.save(oldSession);
            }
        }

        String token = jwtUtils.generateToken(userFound);
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expiration = now.plusMinutes(sessionTimeout);
        UserSession session = UserSession.builder()
        .user(userFound)
        .created_at(LocalDateTime.now())
        .expired_at(expiration)
        .token(token)
        .isActive(true)
        .ipAddress(getClientIP(request))
        .userAgent(request.getHeader("User-Agent"))
        .build();
        sessionRepo.save(session);
        return mapper.toDto(session);
    }

    @Override
    public void invalidateSession(String token) {
        UserSession session = sessionRepo.findByToken(token)
            .orElseThrow(() -> new RuntimeException("Session not found"));          
        session.setActive(false);
        sessionRepo.save(session);
    }
    
    @Override
    public boolean isSessionValid(String token) {
        return sessionRepo.findByTokenAndIsActiveTrue(token)
            .map(session -> !session.getExpired_at().isBefore(LocalDateTime.now()))
            .orElse(false);
    }

    @Override
    public SessionResponseDTO getCurrentSession(String token){
        UserSession session = sessionRepo.findByToken(token)
        .orElseThrow(()-> new ResourceNotFoundException("Session not found"));
        return mapper.toDto(session);
    }

    @Override
    public void deleteAllSession(){

        sessionRepo.deleteAll();
        return;
    }

    private String getClientIP(HttpServletRequest request) {
        String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader == null) {
            return request.getRemoteAddr();
        }
        return xfHeader.split(",")[0];
    }
}
