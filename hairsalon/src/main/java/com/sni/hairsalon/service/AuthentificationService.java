package com.sni.hairsalon.service;

import com.sni.hairsalon.dto.request.ClientSignupRequest;
import com.sni.hairsalon.dto.request.UserRequestDTO;
import com.sni.hairsalon.dto.response.AuthResponse;
import com.sni.hairsalon.dto.response.ClientSignupResponse;
import com.sni.hairsalon.dto.response.UserResponseDTO;

import jakarta.servlet.http.HttpServletRequest;

public interface AuthentificationService {
    public AuthResponse login(UserRequestDTO dto, HttpServletRequest request);
    public void logout(String token);
    public UserResponseDTO signUp(UserRequestDTO user);
    public ClientSignupResponse signupClient(ClientSignupRequest request);  
    public UserResponseDTO signupAdmin(UserRequestDTO request);
    //public void validateUserEmail(String email);
    //public void forgotPassword(String );
}
