package com.sni.hairsalon.mapper;

import org.springframework.stereotype.Component;

import com.sni.hairsalon.dto.request.HaircutRequestDTO;
import com.sni.hairsalon.dto.response.HaircutResponseDTO;
import com.sni.hairsalon.model.Haircut;
import com.sni.hairsalon.utils.ValidationUtils;

@Component
public class HaircutMapper{

    public Haircut toEntity(HaircutRequestDTO request){
        validateField(request);
        Haircut haircut = new Haircut();
        haircut.setType(request.getType());
        haircut.setDuration(request.getDuration());
        haircut.setPrice(request.getPrice());
        
        if(request.getDescription() != null){
            haircut.setDescription(request.getDescription());
        }

        return haircut; 
    }

    public HaircutResponseDTO toDto(Haircut haircut){
        HaircutResponseDTO dto = new HaircutResponseDTO();
        dto.setId(haircut.getId());
        dto.setType(haircut.getType());
        dto.setDuration(haircut.getDuration());
        dto.setPrice(haircut.getPrice());

        if(haircut.getDescription() != null){
            dto.setDescription(haircut.getDescription());
        }

        return dto;
    }
    
    private void validateField(HaircutRequestDTO haircut){
        if(haircut.getType() == null || haircut.getType().isEmpty() || !ValidationUtils.isAlphaWithSpaces(haircut.getType())){
            throw new RuntimeException("Invalid type");
        }
        
        if(!ValidationUtils.isNumber(Integer.toString(haircut.getPrice()))){
            throw new RuntimeException("Invalid price");
        }

        if(!ValidationUtils.isNumber(Integer.toString(haircut.getDuration()))){
            throw new RuntimeException("Invalid duration");
        }
    }
}