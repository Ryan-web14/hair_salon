package com.sni.hairsalon.service;

import java.util.List;

import com.sni.hairsalon.dto.request.HaircutRequestDTO;
import com.sni.hairsalon.dto.response.HaircutResponseDTO;

public interface HaircutService {

    public HaircutResponseDTO createHaircut(HaircutRequestDTO dto);
    public HaircutResponseDTO updateHaircut(long id, HaircutRequestDTO dto);
    public List<HaircutResponseDTO> getAllHaircut();
    public void deleteHaircut(long id);
} 
