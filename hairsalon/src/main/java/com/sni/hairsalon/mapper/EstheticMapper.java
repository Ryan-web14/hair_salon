package com.sni.hairsalon.mapper;

import org.springframework.stereotype.Component;

import com.sni.hairsalon.dto.request.EstheticRequestDTO;
import com.sni.hairsalon.dto.response.EstheticResponseDTO;
import com.sni.hairsalon.model.Esthetic;
import com.sni.hairsalon.utils.ValidationUtils;

@Component
public class EstheticMapper {

    public Esthetic toEntity(EstheticRequestDTO request) {
        validateField(request);
        Esthetic esthetic = new Esthetic();
        esthetic.setType(request.getType());
        esthetic.setDuration(request.getDuration());
        esthetic.setPrice(request.getPrice());
        
        if (request.getDescription() != null) {
            esthetic.setDescription(request.getDescription());
        }

        return esthetic; 
    }

    public EstheticResponseDTO toDto(Esthetic esthetic) {
        EstheticResponseDTO dto = new EstheticResponseDTO();
        dto.setId(esthetic.getId());
        dto.setType(esthetic.getType());
        dto.setDuration(esthetic.getDuration());
        dto.setPrice(esthetic.getPrice());

        if (esthetic.getDescription() != null) {
            dto.setDescription(esthetic.getDescription());
        }

        return dto;
    }
    
    private void validateField(EstheticRequestDTO esthetic) {
        if (esthetic.getType() == null || esthetic.getType().isEmpty() || !ValidationUtils.isAlphaWithSpaces(esthetic.getType())) {
            throw new RuntimeException("Invalid type");
        }
        
        if (!ValidationUtils.isNumber(Integer.toString(esthetic.getPrice()))) {
            throw new RuntimeException("Invalid price");
        }

        if (!ValidationUtils.isNumber(Integer.toString(esthetic.getDuration()))) {
            throw new RuntimeException("Invalid duration");
        }
    }
}