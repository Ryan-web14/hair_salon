package com.sni.hairsalon.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor(staticName = "create")
public class UserRoleResponseDTO {
    private String name;
}
