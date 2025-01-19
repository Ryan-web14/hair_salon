package com.sni.hairsalon.service.serviceImpl;

import org.springframework.stereotype.Service;

import com.sni.hairsalon.dto.request.BarberRequestDTO;
import com.sni.hairsalon.dto.response.BarberResponseDTO;
import com.sni.hairsalon.dto.response.UserResponseDTO;
import com.sni.hairsalon.exception.ResourceNotFoundException;
import com.sni.hairsalon.mapper.BarberMapper;
import com.sni.hairsalon.model.Barber;
import com.sni.hairsalon.model.User;
import com.sni.hairsalon.repository.BarberRepository;
import com.sni.hairsalon.repository.UserRepository;
import com.sni.hairsalon.service.BarberService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BarberServiceImpl implements BarberService {
    
    private final UserRepository userRepo;
    private final BarberRepository barberRepo;
    private final BarberMapper mapper;
    
    @Override
        public BarberResponseDTO createBarber(BarberRequestDTO dto){

            User user = userRepo.findUserByEmail(dto.getEmail())
            .orElseThrow(()-> new ResourceNotFoundException("User not found"));

            Barber barber = Barber.builder()
            .user(user)
            .lastname(dto.getLastname())
            .firstname(dto.getFirstname())
            .phoneNumber(Integer.parseInt(dto.getPhone()))
            .description(dto.getDescription())
            .build();
            barberRepo.save(barber);
            return mapper.toDto(barber);
        }

}
