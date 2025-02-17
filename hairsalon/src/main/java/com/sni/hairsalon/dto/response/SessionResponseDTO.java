package com.sni.hairsalon.dto.response;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SessionResponseDTO {
    private long id;
    private String userId;
    private String token;
    private LocalDateTime createdAt;
    private LocalDateTime expiredAt;
    private boolean isActive;
    private String ipAddress;
    private String userAgent;

}
