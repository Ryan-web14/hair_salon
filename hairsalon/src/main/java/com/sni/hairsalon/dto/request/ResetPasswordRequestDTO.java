package com.sni.hairsalon.dto.request;

import lombok.Data;
import lombok.Getter;

@Data
@Getter
public class ResetPasswordRequestDTO {
    String token;
    String newPassword;
}
