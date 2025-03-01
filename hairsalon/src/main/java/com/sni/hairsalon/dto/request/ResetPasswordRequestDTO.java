package com.sni.hairsalon.dto.request;

import lombok.Data;

@Data
public class ResetPasswordRequestDTO {
    String token;
    String newPassword;
}
