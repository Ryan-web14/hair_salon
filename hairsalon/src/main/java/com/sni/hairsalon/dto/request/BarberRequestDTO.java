package com.sni.hairsalon.dto.request;

import com.sni.hairsalon.model.User;

import lombok.Data;

@Data
public class BarberRequestDTO {
    private String email;
    private String lastname;
    private String firstname;
    private Integer phone;
    private String description;
}
