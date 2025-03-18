package com.sni.hairsalon.dto.request;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(staticName = "create")
public class AvailabilityRequestDTO {
    private String barberId;
    private String estheticianId;
    private LocalDateTime starTime;
    private LocalDateTime endTime;
    private boolean isAvailable;
    private long scheduleId;
    private String note;
}
