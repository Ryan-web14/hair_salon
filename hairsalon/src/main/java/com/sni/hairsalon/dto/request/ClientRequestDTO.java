package com.sni.hairsalon.dto.request;

import com.sni.hairsalon.model.User;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor(staticName = "create")
public class ClientRequestDTO {
    private String email;
    private String firstname;
    private String lastname;
    private int phone;
    private int noShowCount;
}