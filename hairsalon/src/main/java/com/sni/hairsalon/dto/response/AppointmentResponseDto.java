package com.sni.hairsalon.dto.response;

import java.time.LocalDateTime;
import lombok.Data;

 @Data
public class AppointmentResponseDto {
    private Long id;
    private long clientId;
    private String clientLastname;
    private String clientFirstname;
    private long barberId;
    private String barberLastname;
    private String barberFirstname;
    private LocalDateTime bookedTime;
    private String haircutType;
    private int price;
    private int status;
}
