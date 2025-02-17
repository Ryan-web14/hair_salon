package com.sni.hairsalon.service;

import java.util.List;

import com.sni.hairsalon.dto.request.UserRequestDTO;
import com.sni.hairsalon.dto.response.UserResponseDTO;

public interface UserService{
    UserResponseDTO createUser(UserRequestDTO dto);
    UserResponseDTO createAdmin(UserRequestDTO dto);
    UserResponseDTO updateUser(long id, String email);
    UserResponseDTO updatePassword(long id, String newPassword);
    UserResponseDTO getUserById(long id);
    List<UserResponseDTO> getAllUsers();
    public void updateLastLogin(long id);
}

