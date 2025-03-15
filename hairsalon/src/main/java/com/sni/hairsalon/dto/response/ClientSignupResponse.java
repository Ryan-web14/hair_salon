package com.sni.hairsalon.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor  
@AllArgsConstructor
public class ClientSignupResponse{
    private String email;
    private String firstname;
    private String lastname;
    private String phone;

    public ClientSignupResponse (String email){
        this.email = email;
    }


}
