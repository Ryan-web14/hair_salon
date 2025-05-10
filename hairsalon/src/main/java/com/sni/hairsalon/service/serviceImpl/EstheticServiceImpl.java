package com.sni.hairsalon.service.serviceImpl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.sni.hairsalon.dto.request.EstheticRequestDTO;
import com.sni.hairsalon.dto.response.EstheticResponseDTO;
import com.sni.hairsalon.dto.response.EstheticianResponseDTO;
import com.sni.hairsalon.exception.ResourceNotFoundException;
import com.sni.hairsalon.mapper.EstheticMapper;
import com.sni.hairsalon.mapper.EstheticianMapper;
import com.sni.hairsalon.model.Esthetic;
import com.sni.hairsalon.model.User;
import com.sni.hairsalon.repository.EstheticRepository;
import com.sni.hairsalon.repository.EstheticianRepository;
import com.sni.hairsalon.repository.UserRepository;
import com.sni.hairsalon.service.EstheticService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EstheticServiceImpl implements EstheticService {
    
    private final EstheticRepository estheticRepo;
    private final EstheticianRepository estheticianRepo;
    private final UserRepository userRepo;
    private final EstheticianMapper estheticianMapper;
    private final EstheticMapper mapper;

    @Override
    public EstheticResponseDTO createEsthetic(EstheticRequestDTO dto) {
        Esthetic esthetic = Esthetic.builder()
        .type(dto.getType())
        .duration(dto.getDuration())
        .price(dto.getPrice())
        .description(dto.getDescription())
        .build();
        estheticRepo.save(esthetic);
        return mapper.toDto(esthetic);
    }

    @Override
    public EstheticResponseDTO updateEsthetic(long id, EstheticRequestDTO dto) {
        Esthetic esthetic = estheticRepo.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Esthetic not found"));
        esthetic.setType(dto.getType());
        esthetic.setDescription(dto.getDescription());
        esthetic.setPrice(dto.getPrice());
        esthetic.setDuration(dto.getDuration());
        estheticRepo.save(esthetic);
        return mapper.toDto(esthetic);
    }

    @Override
    public List<EstheticResponseDTO> getAllEsthetics() {
        List<EstheticResponseDTO> esthetics = estheticRepo.findAll()
        .stream()
        .map(esthetic -> mapper.toDto(esthetic))
        .collect(Collectors.toList());
        return esthetics;
    }

    @Override
    public EstheticianResponseDTO getEstheticianProfile(String email){

        User user = userRepo.findUserByEmail(email)
        .orElseThrow(()-> new ResourceNotFoundException("No esthetician found"));
        
        return estheticianMapper.toDto(estheticianRepo.findByEmail(email)
        .orElseThrow(()-> new ResourceNotFoundException("no esthetician")));
    
    }

    @Override
    public void deleteEsthetic(long id) {
        estheticRepo.deleteById(id);
    }
    
}