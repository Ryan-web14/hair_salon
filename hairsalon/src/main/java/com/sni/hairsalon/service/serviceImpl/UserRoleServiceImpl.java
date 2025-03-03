package com.sni.hairsalon.service.serviceImpl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sni.hairsalon.dto.request.UserRoleRequestDTO;
import com.sni.hairsalon.dto.response.UserRoleResponseDTO;
import com.sni.hairsalon.service.UserRoleService;

import lombok.RequiredArgsConstructor;

import com.sni.hairsalon.model.UserRole;
import com.sni.hairsalon.repository.UserRoleRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class UserRoleServiceImpl implements UserRoleService{
    
    private final UserRoleRepository roleRepo;

    public UserRoleResponseDTO createRole(UserRoleRequestDTO dto){
        
        UserRole role  =  UserRole.builder()
        .name(dto.getName())
        .description(dto.getDescription())
        .build();

        roleRepo.save(role);

        return UserRoleResponseDTO.create(role.getName());
        
    }
}
