package com.sni.hairsalon.dto.request;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Builder;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(staticName = "create")
public class ClientRequestDTO {
    private String email;
    private String firstname;
    private String lastname;
    private String phone;
    private  int noShowCount;
}

