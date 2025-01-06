package com.sni.hairsalon.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sni.hairsalon.dto.request.UserRequestDTO;
import com.sni.hairsalon.dto.response.UserResponseDTO;
import com.sni.hairsalon.exception.BadRequestException;
import com.sni.hairsalon.model.User;
import com.sni.hairsalon.model.UserRole;
import com.sni.hairsalon.repository.UserRoleRepository;
import com.sni.hairsalon.utils.ValidationUtils;

@Component
public class UserMapper {

    @Autowired
    UserRoleRepository roleRepo;

    public User toEntity(UserRequestDTO request, String roleName){
        validateField(request);
        User user = new User();
        user.setEmail(request.getEmail());
        user.setPasswordHash(request.getPassword());
        user.onCreate();
        UserRole role = roleRepo.findUserRoleByName(roleName);
        user.setRole(role);
        return user;
    }

    public UserResponseDTO toDto(User user){
        UserResponseDTO dto = new UserResponseDTO();
        dto.setId(user.getId());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole().getName());
        return dto;
    }
   
    private void validateField(UserRequestDTO dto){
        if(!ValidationUtils.isValidEmail(dto.getEmail())){
            throw new BadRequestException("Invalid email");
        }
    }
}
