package com.sni.hairsalon.dto.request;



import lombok.Data;

@Data
public class BarberRequestDTO {
    private String email;
    private String lastname;
    private String firstname;
    private String phone;
    private String description;
}
