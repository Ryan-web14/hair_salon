package com.sni.hairsalon.dto.request;

import java.time.LocalDateTime;

import com.sni.hairsalon.model.Barber;
import com.sni.hairsalon.model.Client;
import com.sni.hairsalon.model.Haircut;

import lombok.Data;

@Data
public class AppointmentUpdateRequest {
    private Client client;
    private Barber barber; 
    private Haircut haircut;
    private LocalDateTime appointmentTime;
    private LocalDateTime bookedTime;
 
}
