package com.sni.hairsalon.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.sni.hairsalon.dto.request.AvailabilityRequestDTO;
import com.sni.hairsalon.dto.response.AvailabilityResponseDTO;

public interface AvailabilityService {
    public List<AvailabilityResponseDTO> createAvailability(AvailabilityRequestDTO request);
    public AvailabilityResponseDTO updateAvailabilityStatus(long id, boolean status);
    public List<AvailabilityResponseDTO>getBarberAvailability(long barberId, LocalDate date);
   // public Boolean isAvailableSlot(long id, LocalDateTime startTime, int duration);
    public void makeSlotUnavailable(long barberId, LocalDateTime startTime, int duration);
}


//TODO:  public Boolean isAvailableSlot(long id, LocalDateTime startTime, int duration) fix this