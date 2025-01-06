package com.sni.hairsalon.dto.request;

import lombok.Data;
import lombok.Builder;

@Data
@Builder
public class AuthentificationRequestDTO {
    private String email;
    private String password;
}
