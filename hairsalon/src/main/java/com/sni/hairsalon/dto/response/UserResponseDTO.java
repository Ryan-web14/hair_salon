package com.sni.hairsalon.dto.response;

import com.sni.hairsalon.model.UserRole;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor(staticName = "create")
@NoArgsConstructor
public class UserResponseDTO {
    private long id;
    private String role;
    private String email;  
}