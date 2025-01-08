package com.sni.hairsalon.service;

import com.sni.hairsalon.dto.request.UserRequestDTO;
import com.sni.hairsalon.dto.response.AuthResponse;
import com.sni.hairsalon.dto.response.UserResponseDTO;

import jakarta.servlet.http.HttpServletRequest;

public interface AuthentificationService {
    public AuthResponse login(UserRequestDTO dto, HttpServletRequest request);
    public void logout(String token);
    public UserResponseDTO signUp(UserRequestDTO user);
}
