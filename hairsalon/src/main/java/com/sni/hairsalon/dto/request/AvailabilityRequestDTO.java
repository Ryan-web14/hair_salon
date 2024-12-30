package com.sni.hairsalon.dto.request;

import java.time.LocalDateTime;

import com.sni.hairsalon.model.Availability;
import com.sni.hairsalon.model.Barber;

import lombok.Data;

@Data
public class AvailabilityRequestDTO {
    private Barber barber;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private boolean isAvailable;
    private String note;

    public Availability toEntity(AvailabilityRequestDTO availabilityRequest){
        Availability availability = new Availability();
        availability.setBarber(availabilityRequest.getBarber());
        availability.setStartTime(availabilityRequest.getStartTime());
        availability.setEndTime(availabilityRequest.getEndTime());
        availability.set_available(availabilityRequest.isAvailable());
        availability.setNote(availabilityRequest.getNote());
        return availability;
    }
}
