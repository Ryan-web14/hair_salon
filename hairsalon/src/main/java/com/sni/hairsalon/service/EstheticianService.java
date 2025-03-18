package com.sni.hairsalon.service;

import java.util.List;

import com.sni.hairsalon.dto.request.EstheticianRequestDTO;
import com.sni.hairsalon.dto.response.EstheticianResponseDTO;

public interface EstheticianService {
    
    public EstheticianResponseDTO createEsthetician(EstheticianRequestDTO dto);
    public List<EstheticianResponseDTO> getAllEstheticians();
    public EstheticianResponseDTO updateEsthetician(Long id, EstheticianRequestDTO request);
    public void deleteEsthetician(long id);
    public EstheticianResponseDTO getEstheticianById(long id);
    public EstheticianResponseDTO getEstheticianProfile(String email);
    public EstheticianResponseDTO createEstheticianByAdmin(EstheticianRequestDTO dto);
    public List<EstheticianResponseDTO> getAllAvailableEstheticians();
}