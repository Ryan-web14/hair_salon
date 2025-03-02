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
    private LocalDateTime starTime;
    private LocalDateTime endTime;
    private boolean isAvailable;
    private String note;
}
