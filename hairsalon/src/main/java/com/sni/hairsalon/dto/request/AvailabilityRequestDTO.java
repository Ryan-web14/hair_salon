package com.sni.hairsalon.dto.request;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AvailabilityRequestDTO {
    private String barberId;
    private LocalDateTime starTime;
    private LocalDateTime endTime;
    private boolean isAvailable;
    private String note;
}
