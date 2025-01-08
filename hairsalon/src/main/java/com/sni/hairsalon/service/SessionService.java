package com.sni.hairsalon.service;

import com.sni.hairsalon.dto.response.SessionResponseDTO;
import com.sni.hairsalon.dto.response.UserResponseDTO;

import jakarta.servlet.http.HttpServletRequest;

public interface SessionService {

    public SessionResponseDTO createSession(HttpServletRequest request, UserResponseDTO user);
    public void invalidateSession(String token);
    public boolean isSessionValid(String token);
    public SessionResponseDTO getCurrentSession(String token);
    //public List<SessionResponseDTO> getAllSession();
    
}
