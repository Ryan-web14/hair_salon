package com.sni.hairsalon.dto.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor(staticName = "create")
@NoArgsConstructor
public class UserResponseDTO {
    private String id;
    private String email;  
}
