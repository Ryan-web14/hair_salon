package com.sni.hairsalon.service;

import com.sni.hairsalon.dto.request.ClientRequestDTO;
import com.sni.hairsalon.dto.response.ClientResponseDTO;

public interface ClientService {
    
    public ClientResponseDTO createClient(ClientRequestDTO dto);
}
