package com.sni.hairsalon.service;

import java.util.List;

import com.sni.hairsalon.dto.request.UserRequestDTO;
import com.sni.hairsalon.dto.response.UserResponseDTO;

public interface UserService{
    UserResponseDTO createUser(UserRequestDTO dto);
    UserResponseDTO updateUser(long id, UserRequestDTO dto);
    UserResponseDTO updatePassword(long id, String newPassword);
    UserResponseDTO getUserById(long id);
    void deleteUser(long id);
    List<UserResponseDTO> getAllUsers();
}

