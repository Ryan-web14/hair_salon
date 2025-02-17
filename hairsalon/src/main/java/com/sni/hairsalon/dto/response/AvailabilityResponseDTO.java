package com.sni.hairsalon.dto.response;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class AvailabilityResponseDTO {
    private long id;
    private String barberId;
    private String firstname;
    private String lastName;
    private LocalDateTime starTime;
    private LocalDateTime endTime;
    private boolean isAvailable;
    private String note;
}
