package com.sni.hairsalon.dto.request;

import java.time.LocalDateTime;

import com.sni.hairsalon.model.Barber;
import com.sni.hairsalon.model.Client;
import com.sni.hairsalon.model.Haircut;
import com.sni.hairsalon.model.Appointment;
import com.sni.hairsalon.model.Status;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor(staticName = "create")
public class AppointmentRequestDTO {
    private Client client;
    private Barber barber; 
    private Haircut haircut;
    private LocalDateTime appointmentTime;
    private Status status = Status.PENDING;
    //private LocalDateTime bookedTime;


    public Appointment toEntity(){
        Appointment appointment = new Appointment();
        appointment.setClient(client);
        appointment.setBarber(barber);
        appointment.setHaircut(haircut);
        appointment.setAppointmentTime(appointmentTime);
        appointment.setStatus(status.getCode());
        return appointment;
    }
    
}
