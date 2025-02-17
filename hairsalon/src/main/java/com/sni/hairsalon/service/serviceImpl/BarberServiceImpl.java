package com.sni.hairsalon.service.serviceImpl;

import java.util.List;
import java.util.stream.Collectors;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sni.hairsalon.dto.request.BarberRequestDTO;
import com.sni.hairsalon.dto.response.BarberResponseDTO;
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

    @Override
    @Transactional(readOnly = true) 
    public List<BarberResponseDTO> getAllBarber(){

        List<Barber> barbers =  barberRepo.findAll();

        return barbers.stream()
        .map(barber->mapper.toDto(barber))
        .collect(Collectors.toList());
    }
 
    @Override
    public BarberResponseDTO updateBarber(String id, BarberRequestDTO requestDTO){
        Barber barber = barberRepo.findById(Long.parseLong(id))
            .orElseThrow(() -> new ResourceNotFoundException("Barber not found"));

        barber.setLastname(requestDTO.getLastname());
        barber.setFirstname(requestDTO.getFirstname());
        barber.setPhoneNumber(Integer.parseInt(requestDTO.getPhone()));
        barber.setDescription(requestDTO.getDescription());

        barberRepo.save(barber);

        return mapper.toDto(barber);
    }
    
    @Override
    public void deleteBarber(long id) {
        Barber barber = barberRepo.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Barber not found"));
        barberRepo.delete(barber);
    }

    @Override
    @Transactional(readOnly = true)
    public BarberResponseDTO getBarberById(long id) {
        Barber barber = barberRepo.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Barber not found"));
        return mapper.toDto(barber);
    }

}

