package com.sni.hairsalon.dto.request;

import java.time.LocalDateTime;


import com.sni.hairsalon.model.Status;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor(staticName = "create")
@Builder
public class AppointmentRequestDTO {
    private String email;
    private String barberId; 
    private String haircutType;
    private LocalDateTime appointmentTime;
    
    @Builder.Default
    private Status status = Status.PENDING;
}
