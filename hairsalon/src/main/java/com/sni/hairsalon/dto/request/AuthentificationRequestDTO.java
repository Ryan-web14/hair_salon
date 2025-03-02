package com.sni.hairsalon.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(staticName = "create")
public class AuthentificationRequestDTO {
    private String email;
    private String password;
}
