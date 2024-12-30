package com.sni.hairsalon.mapper;

import org.springframework.beans.factory.annotation.Autowired;

import com.sni.hairsalon.dto.request.BarberRequestDTO;
import com.sni.hairsalon.dto.response.BarberResponseDTO;
import com.sni.hairsalon.model.Barber;
import com.sni.hairsalon.model.User;
import com.sni.hairsalon.repository.UserRepository;
import com.sni.hairsalon.utils.ValidationUtils;

public class BarberMapper {

    @Autowired
    UserRepository userRepo;
    
    public Barber toEntity(BarberRequestDTO dto){
        validateField(dto);
        User user = userRepo.findUserByEmail(dto.getEmail());
        Barber barber = new Barber();
        barber.setUser(user);
        barber.setFirstname(dto.getFirstname());
        barber.setLastname(dto.getLastname());
        barber.setPhoneNumber(dto.getPhone());

        if(dto.getDescription() != null){
            barber.setDescription(dto.getDescription());
        }

        return barber;
    }

    public BarberResponseDTO toDto(Barber barber){
        BarberResponseDTO dto = new BarberResponseDTO();
        dto.setId(barber.getId());
        dto.setEmail(barber.getUser().getEmail());
        dto.setFirstname(dto.getFirstname());
        dto.setLastname(dto.getLastname());
        dto.setPhone(barber.getPhoneNumber());
        return dto;
    }

     public void validateField(BarberRequestDTO barberRequest){
        if(barberRequest.getFirstname().isEmpty() || barberRequest.getLastname().isEmpty()){
            throw new RuntimeException("Empty name");
        }   
    
    
        if(!ValidationUtils.isLetter(barberRequest.getFirstname()) || ValidationUtils.isLetter(barberRequest.getLastname())){
            throw new RuntimeException("Names are invalid");
          
        }
       
        String phone = Integer.toString(barberRequest.getPhone()); 
        boolean verifiedPhone = ValidationUtils.isValidPhone(phone);
        if(!verifiedPhone || phone == null){
             throw new RuntimeException("Invalid phone number"+ phone);
            
    }
  }
}
