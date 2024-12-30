package com.sni.hairsalon.dto.request;

import lombok.Data;

@Data
public class UserRequestDTO {
    private String email;
    private String role;
    private String password;
}
