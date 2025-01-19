package com.sni.hairsalon.dto.request;

import java.time.LocalDateTime;


import com.sni.hairsalon.model.Status;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor(staticName = "create")
public class AppointmentRequestDTO {
    private long clientId;
    private long barberId; 
    private long haircutId;
    private LocalDateTime appointmentTime;
    private Status status = Status.PENDING;
}
