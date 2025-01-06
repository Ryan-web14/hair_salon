package com.sni.hairsalon.service;

import com.sni.hairsalon.dto.request.SessionRequestDTO;
import com.sni.hairsalon.dto.request.UserRequestDTO;
import com.sni.hairsalon.dto.response.SessionResponseDTO;
import com.sni.hairsalon.dto.response.UserResponseDTO;
import com.sni.hairsalon.model.User;

public interface SessionService {

    public SessionResponseDTO createSession(SessionRequestDTO dto, UserResponseDTO user);
    public void deleteSession(long sessionId);
    public void invalidateSession(long sessionId);
    
}
