package com.sni.hairsalon.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(staticName = "create")
public class ClientSignupRequest {    
    private String email;
    private String password;
    private String firstname;
    private String lastname;
    private String phone;
}
