package com.sni.hairsalon.mapper;

import org.springframework.stereotype.Component;

import com.sni.hairsalon.dto.request.EstheticianRequestDTO;
import com.sni.hairsalon.dto.response.EstheticianResponseDTO;
import com.sni.hairsalon.exception.ResourceNotFoundException;
import com.sni.hairsalon.model.Esthetician;
import com.sni.hairsalon.model.User;
import com.sni.hairsalon.repository.UserRepository;
import com.sni.hairsalon.utils.ValidationUtils;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class EstheticianMapper {

    private final UserRepository userRepo;
    
    public Esthetician toEntity(EstheticianRequestDTO dto){
        validateField(dto);
        User user = userRepo.findUserByEmail(dto.getEmail())
        .orElseThrow(()-> new ResourceNotFoundException("Esthetician not found"));
        Esthetician esthetician = new Esthetician();
        esthetician.setUser(user);
        esthetician.setFirstname(dto.getFirstname());
        esthetician.setLastname(dto.getLastname());
        esthetician.setPhoneNumber(Integer.parseInt(dto.getPhone()));
        
        if(dto.getDescription() != null){
            esthetician.setDescription(dto.getDescription());
        }
        
        return esthetician;
    }
    
    public EstheticianResponseDTO toDto(Esthetician esthetician){
        EstheticianResponseDTO dto = new EstheticianResponseDTO();
        dto.setId(String.valueOf(esthetician.getId()));
        dto.setEmail(esthetician.getUser().getEmail());
        dto.setFirstname(esthetician.getFirstname());
        dto.setLastname(esthetician.getLastname());
        dto.setPhone(String.valueOf(esthetician.getPhoneNumber()));
        dto.setAvailable(esthetician.isAvailable());
        return dto;
    }
    
    public void validateField(EstheticianRequestDTO estheticianRequest){
        if(estheticianRequest.getFirstname().isEmpty() || estheticianRequest.getLastname().isEmpty()){
            throw new RuntimeException("Empty name");
        }
        
        if(!ValidationUtils.isLetter(estheticianRequest.getFirstname()) || ValidationUtils.isLetter(estheticianRequest.getLastname())){
            throw new RuntimeException("Names are invalid");
        }
        
        String phone = estheticianRequest.getPhone();
        
        boolean verifiedPhone = ValidationUtils.isValidPhone(phone);
        if(!verifiedPhone || phone == null){
            throw new RuntimeException("Invalid phone number"+ phone);
        }
    }
}