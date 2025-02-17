package com.sni.hairsalon.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SessionRequestDTO {
    private String ipAddress;
    private String userAgent;
}
