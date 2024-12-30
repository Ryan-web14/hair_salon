package com.sni.hairsalon.dto.response;

import com.sni.hairsalon.model.Client;

import lombok.Data;

@Data
public class ClientResponseDTO {
    private long id;
    private String email;
    private String firstname;
    private String lastname;
    private int phone;
    private int noShowCount;
}
