package com.sni.hairsalon.dto.request;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Builder;
@Data
@Builder
@AllArgsConstructor(staticName = "create")
public class ClientRequestDTO {
    private String email;
    private String firstname;
    private String lastname;
    private String phone;
   @Builder.Default private int noShowCount = 0;
}


//admettre une valeur par defaut a noshowcount