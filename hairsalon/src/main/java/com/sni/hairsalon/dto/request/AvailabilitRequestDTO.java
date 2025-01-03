package com.sni.hairsalon.dto.request;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;

@Data
public class AvailabilitRequestDTO {
    private long barberId;
    private LocalDateTime starTime;
    private LocalDateTime endTime;
    private boolean isAvailable;
    private String note;
}
