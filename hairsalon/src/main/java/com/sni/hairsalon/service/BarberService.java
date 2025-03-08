package com.sni.hairsalon.service;

import java.util.List;

import com.sni.hairsalon.dto.request.BarberRequestDTO;
import com.sni.hairsalon.dto.response.BarberResponseDTO;

public interface  BarberService {
    
    public BarberResponseDTO createBarber(BarberRequestDTO dto);
    public List<BarberResponseDTO> getAllBarber();
     public BarberResponseDTO updateBarber(Long id, BarberRequestDTO request);
    public void   deleteBarber(long id);
    public BarberResponseDTO getBarberById(long id);
    public BarberResponseDTO getBarberProfile(String email);
    public BarberResponseDTO createBarberByAdmin(BarberRequestDTO dto);
}
