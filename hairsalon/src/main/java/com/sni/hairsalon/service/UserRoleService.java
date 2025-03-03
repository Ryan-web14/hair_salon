package com.sni.hairsalon.service;

import com.sni.hairsalon.dto.request.UserRoleRequestDTO;
import com.sni.hairsalon.dto.response.UserRoleResponseDTO;

public interface UserRoleService {
    
    public UserRoleResponseDTO createRole(UserRoleRequestDTO dto);
}
