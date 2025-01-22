package com.sni.hairsalon.dto.request;

import java.time.LocalDateTime;


import com.sni.hairsalon.model.Status;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AppointmentRequestDTO {
    private long clientId;
    private long barberId; 
    private long haircutId;
    private LocalDateTime appointmentTime;
    
    @Builder.Default
    private Status status = Status.PENDING;
}
