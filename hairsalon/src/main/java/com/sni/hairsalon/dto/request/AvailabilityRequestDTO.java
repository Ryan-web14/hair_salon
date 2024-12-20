package com.sni.hairsalon.dto.request;

import java.time.LocalDateTime;

import com.sni.hairsalon.model.Barber;

import lombok.Data;

@Data
public class AvailabilityRequestDTO {
    private Barber barberId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private boolean isAvailable;
    private String note;
}
