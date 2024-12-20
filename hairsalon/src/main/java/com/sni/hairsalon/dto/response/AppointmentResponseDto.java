package com.sni.hairsalon.dto.response;

import java.time.LocalDateTime;

import com.sni.hairsalon.model.Appointment;

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

    public AppointmentResponseDto toDto(Appointment appointment){
           AppointmentResponseDto dto = new AppointmentResponseDto();
           dto.setId(appointment.getId());
           dto.setClientId(appointment.getClient().getId());
           dto.setClientFirstname(appointment.getClient().getFirstname());
           dto.setClientLastname(appointment.getClient().getLastname());
           dto.setBarberId(appointment.getBarber().getId());
           dto.setBarberFirstname(appointment.getBarber().getFirstname());
           dto.setBarberLastname(appointment.getBarber().getLastname());
           dto.setBookedTime(appointment.getAppointmentTime());
           dto.setHaircutType(appointment.getHaircut().getType());
           dto.setPrice(appointment.getHaircut().getPrice());
           dto.setPrice(appointment.getStatus());
           return dto;
    }
}
