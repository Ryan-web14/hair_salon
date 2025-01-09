package com.sni.hairsalon.service;

import java.time.LocalDate;
import java.util.List;

import com.sni.hairsalon.dto.request.AvailabilitRequestDTO;
import com.sni.hairsalon.dto.response.AvailabilityResponseDTO;

public interface AvailabilityService {
    public List<AvailabilityResponseDTO> createAvailability(AvailabilitRequestDTO dto);
    public AvailabilityResponseDTO updateAvailabilityStatus(long id, boolean status);
    public List<AvailabilityResponseDTO>getBarberAvailability(long barberId, LocalDate date);
}
