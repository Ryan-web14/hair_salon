package com.sni.hairsalon.mapper;

import org.springframework.stereotype.Component;

import com.sni.hairsalon.dto.request.BarberRequestDTO;
import com.sni.hairsalon.dto.response.BarberResponseDTO;
import com.sni.hairsalon.exception.ResourceNotFoundException;
import com.sni.hairsalon.model.Barber;
import com.sni.hairsalon.model.User;
import com.sni.hairsalon.repository.UserRepository;
import com.sni.hairsalon.utils.ValidationUtils;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class BarberMapper {

    private final UserRepository userRepo;
    
    public Barber toEntity(BarberRequestDTO dto){
        validateField(dto);
        User user = userRepo.findUserByEmail(dto.getEmail())
        .orElseThrow(()-> new ResourceNotFoundException("Barber not found"));
        Barber barber = new Barber();
        barber.setUser(user);
        barber.setFirstname(dto.getFirstname());
        barber.setLastname(dto.getLastname());
        barber.setPhoneNumber(Integer.parseInt(dto.getPhone()));

        if(dto.getDescription() != null){
            barber.setDescription(dto.getDescription());
        }

        return barber;
    }

    public BarberResponseDTO toDto(Barber barber){
        BarberResponseDTO dto = new BarberResponseDTO();
        dto.setId(String.valueOf(barber.getId()));
        dto.setEmail(barber.getUser().getEmail());
        dto.setFirstname(barber.getFirstname());
        dto.setLastname(barber.getLastname());
        dto.setPhone(String.valueOf(barber.getPhoneNumber()));
        dto.setAvailable(barber.isAvailable());
        return dto;
    }

     public void validateField(BarberRequestDTO barberRequest){
        if(barberRequest.getFirstname().isEmpty() || barberRequest.getLastname().isEmpty()){
            throw new RuntimeException("Empty name");
        }   
    
    
        if(!ValidationUtils.isLetter(barberRequest.getFirstname()) || ValidationUtils.isLetter(barberRequest.getLastname())){
            throw new RuntimeException("Names are invalid");
          
        }
       
        String phone = barberRequest.getPhone(); 
        boolean verifiedPhone = ValidationUtils.isValidPhone(phone);
        if(!verifiedPhone || phone == null){
             throw new RuntimeException("Invalid phone number"+ phone);
            
    }
  }
}
