package com.sni.hairsalon.service;

import java.util.List;

import com.sni.hairsalon.dto.request.EstheticRequestDTO;
import com.sni.hairsalon.dto.response.EstheticResponseDTO;
import com.sni.hairsalon.dto.response.EstheticianResponseDTO;

public interface EstheticService {
    
    public EstheticResponseDTO createEsthetic(EstheticRequestDTO dto);
    EstheticResponseDTO updateEsthetic(long id, EstheticRequestDTO dto);
    public List<EstheticResponseDTO> getAllEsthetics();
    public void deleteEsthetic(long id);

}
