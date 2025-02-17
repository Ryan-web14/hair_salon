package com.sni.hairsalon.dto.response;

import lombok.Data;

@Data
public class ClientResponseDTO {
    private String id;
    private String email;
    private String firstname;
    private String lastname;
    private String phone;
    private int noShowCount;
}
