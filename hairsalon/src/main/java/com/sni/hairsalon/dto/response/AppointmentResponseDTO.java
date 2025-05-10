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
public class AppointmentResponseDTO {
    private String id;
    private String clientId;
    private String clientLastname;
    private String clientFirstname;
    private String clientEmail;
    private String clientPhone;
    private String barberId;
    private String barberLastname;
    private String barberFirstname;
    private String barberEmail;
    private String barberPhone;
    private String estheticianId;
    private String estheticianLastname;
    private String estheticianFirstname;
    private String estheticianEmail;
    private String estheticianPhone;
    private LocalDateTime appointmentTime;
    private LocalDateTime bookedTime;
    private String haircutType;
    private String duration;
    private String estheticType;
    private int price;
    private String status;
}