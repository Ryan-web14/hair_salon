package com.sni.hairsalon.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Builder;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(staticName = "create")
public class EstheticianRequestDTO {
    private String email;
    private String lastname;
    private String firstname;
    private String phone;
    private String description;
    private boolean available;
}