package com.sni.hairsalon.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor(staticName = "create")
@NoArgsConstructor
public class BarberResponseDTO {
    private String id;
    private String email;
    private String firstname;
    private String lastname;
    private String phone;
    private String description;
    private boolean available;
}
