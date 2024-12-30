package com.sni.hairsalon.dto.request;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;

import com.sni.hairsalon.model.Barber;
import com.sni.hairsalon.model.Haircut;
import com.sni.hairsalon.repository.AppointmentRepository;
import com.sni.hairsalon.repository.BarberRepository;
import com.sni.hairsalon.model.Appointment;
import com.sni.hairsalon.exception.ResourceNotFoundException;

import lombok.Builder;
import lombok.Data;
@Data
@Builder
public class AppointmentUpdateRequest {
    private Long id;
    private Long barberId;
    private Long clientId;
    private LocalDateTime appointmentTime;
    private Long haircutId;
    private Integer status;
    
    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private BarberRepository barberRepository;

    public Appointment updateAppointment(AppointmentRequestDTO dto, Appointment entity){
        
        if(dto.getId() == entity.getId()){
            if(dto.getClient() != null
            && dto.getClient().getId() == entity.getClient().getId()){
                 appointmentRepository.findAppointmentById(entity.getId())
                .orElseThrow(()-> new ResourceNotFoundException("client not found"));

                if(dto.getBarber() != null){
                     barberRepository.findBarberById(dto.getBarber().getId())
                    .orElseThrow(()-> new ResourceNotFoundException("barber not found"));
                }

                entity.setBarber(dto.getBarber());

                if(dto.getAppointmentTime() != null){
                    entity.setAppointmentTime(dto.getAppointmentTime());
                }

                if(dto.getHaircut() != null){
                    entity.setHaircut(dto.getHaircut());
                }

                if(dto.getStatus() != null){
                    entity.setStatus(dto.getStatus().getCode());
                }
            }
        }
        return entity;     
    }
}
