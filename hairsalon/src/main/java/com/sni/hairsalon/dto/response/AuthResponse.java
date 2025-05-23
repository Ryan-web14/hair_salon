package com.sni.hairsalon.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class AuthResponse {
    private String token;
    private String email;
    private String role;

    public AuthResponse(String token, String email){
        this.token = token;
        this.email = email;
    }
    
}
