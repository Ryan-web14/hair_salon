package com.sni.hairsalon.dto.response;

import java.time.LocalDateTime;

import com.sni.hairsalon.model.Appointment;

public class AppointmentResponseDto {
    private Long id;
    private String clientName;
    private String barberName;
    private LocalDateTime bookedTime;
    private String haircutType;
    private int price;
    private int status;

    /*public AppointmentResponseDto toDto(Appointment appointment){
        
    }*/
}
