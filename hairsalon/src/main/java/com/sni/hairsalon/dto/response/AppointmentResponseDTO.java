package com.sni.hairsalon.dto.response;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;

 @Data
 @Builder
public class AppointmentResponseDTO {
    private String id;
    private String clientId;
    private String clientLastname;
    private String clientFirstname;
    private String clientEmail;
    private String barberId;
    private String barberLastname;
    private String barberFirstname;
    private String barberEmail;
   private LocalDateTime appointmentTime;
    private LocalDateTime bookedTime;
    private String haircutType;
    private int price;
    private String status;
}
