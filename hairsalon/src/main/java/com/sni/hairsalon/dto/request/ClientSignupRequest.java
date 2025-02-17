package com.sni.hairsalon.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ClientSignupRequest {    
    private String email;
    private String password;
    private String firstname;
    private String lastname;
    private String phone;
}
