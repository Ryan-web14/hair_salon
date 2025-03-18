package com.sni.hairsalon.dto.response;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(staticName = "create")
public class AvailabilityResponseDTO {
    private long id;
    private String barberId;
    private String estheticianId;
    private String firstname;
    private String lastName;
    private LocalDateTime starTime;
    private LocalDateTime endTime;
    private boolean isAvailable;
    private String note;
}
