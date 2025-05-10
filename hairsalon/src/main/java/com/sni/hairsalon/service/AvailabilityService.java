package com.sni.hairsalon.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.sni.hairsalon.dto.request.AvailabilityRequestDTO;
import com.sni.hairsalon.dto.response.AvailabilityResponseDTO;
import com.sni.hairsalon.model.UserPrincipal;

public interface AvailabilityService {
    public List<AvailabilityResponseDTO> createAvailability(AvailabilityRequestDTO request);
    public AvailabilityResponseDTO updateAvailabilityStatus(long id, boolean status);
    public List<AvailabilityResponseDTO>getBarberAvailability(long barberId, LocalDate date);
    public List<AvailabilityResponseDTO> getEstheticianAvailability(long estheticianId, LocalDate date);
    public Boolean isAvailableSlot(String providerType, long providerId, LocalDateTime startTime, int duration);
   public void makeProviderSlotUnavailable(String providerType, long providerId, LocalDateTime startTime, int duration);
   public void deleteAllAvailability();
   public List<AvailabilityResponseDTO> getProviderAvailability(UserPrincipal authenticatedUser, LocalDate date);
}


//TODO:  public Boolean isAvailableSlot(long id, LocalDateTime startTime, int duration) fix this