package com.sni.hairsalon.dto.response;

import java.util.List;

import com.sni.hairsalon.model.Appointment;
import com.sni.hairsalon.model.Schedule;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor(staticName = "create")
@NoArgsConstructor
public class BarberCompleteResponseDTO {
    private String id;
    private String email;
    private String firstname;
    private String lastname;
    private String phone;
    private List<Appointment> appointments;
    private List<Schedule> schedules;
}
