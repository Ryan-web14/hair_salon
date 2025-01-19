package com.sni.hairsalon.service;

import com.sni.hairsalon.dto.request.BarberRequestDTO;
import com.sni.hairsalon.dto.response.BarberResponseDTO;

public interface  BarberService {
    
    public BarberResponseDTO createBarber(BarberRequestDTO dto);
}
