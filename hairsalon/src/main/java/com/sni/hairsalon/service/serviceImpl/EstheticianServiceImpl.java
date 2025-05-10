package com.sni.hairsalon.service.serviceImpl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sni.hairsalon.dto.request.EstheticianRequestDTO;
import com.sni.hairsalon.dto.response.EstheticianResponseDTO;
import com.sni.hairsalon.exception.ResourceNotFoundException;
import com.sni.hairsalon.mapper.EstheticianMapper;
import com.sni.hairsalon.model.Esthetician;
import com.sni.hairsalon.model.User;
import com.sni.hairsalon.repository.EstheticianRepository;
import com.sni.hairsalon.service.EstheticianService;
import com.sni.hairsalon.service.UserService;
import com.sni.hairsalon.utils.ValidationUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EstheticianServiceImpl implements EstheticianService {
    
    private final UserService userService;
    private final EstheticianRepository estheticianRepo;
    private final EstheticianMapper mapper;
    
    @Override
    @Transactional
    public EstheticianResponseDTO createEsthetician(EstheticianRequestDTO dto) {
        validateField(dto); 
        User user = userService.getUserByEmail(dto.getEmail());
        Esthetician esthetician = Esthetician.builder()
            .user(user)
            .lastname(dto.getLastname().toUpperCase())
            .firstname(dto.getFirstname())
            .phoneNumber(Integer.parseInt(dto.getPhone()))
            .description(dto.getDescription())
            .build();
        estheticianRepo.save(esthetician);
        return mapper.toDto(esthetician);
    }

    @Override
    @Transactional
    public EstheticianResponseDTO createEstheticianByAdmin(EstheticianRequestDTO dto) {
        validateField(dto);
        Long userId = userService.createEstheticianUserByAdmin(dto);
        User user = userService.getUserById(userId);
        Esthetician esthetician = Esthetician.builder()
            .user(user)
            .lastname(dto.getLastname().toUpperCase())
            .firstname(dto.getFirstname())
            .phoneNumber(Integer.parseInt(dto.getPhone()))
            .available(dto.isAvailable())
            .description(dto.getDescription())
            .build();
        estheticianRepo.save(esthetician);
        return mapper.toDto(esthetician);
    }   

    @Override
    @Transactional(readOnly = true) 
    public List<EstheticianResponseDTO> getAllEstheticians() {
        List<Esthetician> estheticians = estheticianRepo.findAll();

        return estheticians.stream()
            .map(esthetician -> mapper.toDto(esthetician))
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<EstheticianResponseDTO> getAllAvailableEstheticians() {
        List<Esthetician> estheticians = estheticianRepo.findByAvailable(true);

        return estheticians.stream()
            .map(esthetician -> mapper.toDto(esthetician))
            .collect(Collectors.toList());
    }
 
    @Override
    @Transactional
    public EstheticianResponseDTO updateEsthetician(Long id, EstheticianRequestDTO requestDTO) {
        validateField(requestDTO);
        Esthetician esthetician = estheticianRepo.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Esthetician not found"));

        esthetician.setLastname(requestDTO.getLastname().toUpperCase());
        esthetician.setFirstname(requestDTO.getFirstname());
        esthetician.setPhoneNumber(Integer.parseInt(requestDTO.getPhone()));
        esthetician.setAvailable(requestDTO.isAvailable());
        esthetician.setDescription(requestDTO.getDescription());

        User user = userService.getUserById(esthetician.getUser().getId());
        userService.updateUser(user.getId(), requestDTO.getEmail());
 
        estheticianRepo.save(esthetician);

        return mapper.toDto(esthetician);
    }
    
    @Override
    public void deleteEsthetician(long id) {
        Esthetician esthetician = estheticianRepo.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Esthetician not found"));
        estheticianRepo.delete(esthetician);
    }

    @Override
    @Transactional(readOnly = true)
    public EstheticianResponseDTO getEstheticianById(long id) {
        Esthetician esthetician = estheticianRepo.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Esthetician not found"));
        return mapper.toDto(esthetician);
    }

    @Override
    @Transactional(readOnly = true)
    public EstheticianResponseDTO getEstheticianProfile(String email) {
        User user = userService.getUserByEmail(email);

        Esthetician esthetician = estheticianRepo.findByUserId(user.getId())
            .orElseThrow(() -> new ResourceNotFoundException("Esthetician not found"));

        return mapper.toDto(esthetician);
    }

    private void validateField(EstheticianRequestDTO requestDTO) {
        if(requestDTO.getFirstname().isEmpty() || requestDTO.getLastname().isEmpty()) {
            throw new RuntimeException("Empty name");
        }   

        if(!ValidationUtils.isLetter(requestDTO.getFirstname()) || !ValidationUtils.isLetter(requestDTO.getLastname())) {
            throw new RuntimeException("Names are invalid");
        }
       
        String phone = requestDTO.getPhone(); 
        boolean verifiedPhone = ValidationUtils.isValidPhone(phone);
        if(!verifiedPhone || phone == null) {
            throw new RuntimeException("Invalid phone number" + phone);      
        }
    }
}