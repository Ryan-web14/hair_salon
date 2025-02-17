package com.sni.hairsalon.mapper;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sni.hairsalon.dto.request.SessionRequestDTO;
import com.sni.hairsalon.dto.response.SessionResponseDTO;
import com.sni.hairsalon.dto.response.UserResponseDTO;
import com.sni.hairsalon.exception.ResourceNotFoundException;
import com.sni.hairsalon.model.User;
import com.sni.hairsalon.model.UserSession;
import com.sni.hairsalon.repository.UserRepository;

@Component
public class SessionMapper {

    @Autowired
    private UserRepository userRepo;

    public UserSession toEntity(UserResponseDTO dto,SessionRequestDTO request){
        User user =  userRepo.findUserById(Long.parseLong(dto.getId()))
        .orElseThrow(()-> new ResourceNotFoundException("User not found"));
        UserSession session = UserSession.builder()
        .user(user)
        .created_at(LocalDateTime.now())
        .isActive(true)
        .ipAddress(request.getIpAddress())
        .userAgent(request.getUserAgent())
        .build();
        return session;
    }

    public SessionResponseDTO toDto(UserSession session){
        SessionResponseDTO dto = SessionResponseDTO.builder()
        .id(session.getId())
        .userId(String.valueOf(session.getUser().getId()))
        .createdAt(session.getCreated_at())
        .expiredAt(session.getExpired_at())
        .token(session.getToken())
        .isActive(session.isActive())
        .ipAddress(session.getIpAddress())
        .userAgent(session.getUserAgent())
        .build();
        return dto;
    }
}
