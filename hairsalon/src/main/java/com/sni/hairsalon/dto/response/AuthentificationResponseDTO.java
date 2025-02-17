package com.sni.hairsalon.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthentificationResponseDTO {
    private String token;
    private String email;
}
