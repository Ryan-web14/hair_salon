package com.sni.hairsalon.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(staticName = "create")
public class ClientResponseDTO {
    private String id;
    private String email;
    private String firstname;
    private String lastname;
    private String phone;
    private int noShowCount;
}
