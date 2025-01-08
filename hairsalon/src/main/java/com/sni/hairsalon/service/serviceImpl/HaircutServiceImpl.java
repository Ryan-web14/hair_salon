package com.sni.hairsalon.service.serviceImpl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.sni.hairsalon.dto.request.HaircutRequestDTO;
import com.sni.hairsalon.dto.response.HaircutResponseDTO;
import com.sni.hairsalon.exception.ResourceNotFoundException;
import com.sni.hairsalon.mapper.HaircutMapper;
import com.sni.hairsalon.model.Haircut;
import com.sni.hairsalon.repository.HaircutRepository;
import com.sni.hairsalon.service.HaircutService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HaircutServiceImpl implements HaircutService {
    
    private final HaircutRepository haircutRepo;
    private final HaircutMapper mapper;

    @Override
    public HaircutResponseDTO createHaircut(HaircutRequestDTO dto){
        Haircut haircut = Haircut.builder()
        .type(dto.getType())
        .duration(dto.getDuration())
        .price(dto.getPrice())
        .description(dto.getDescription())
        .build();
        haircutRepo.save(haircut);
        return mapper.toDto(haircut);
    }

    @Override
    public HaircutResponseDTO updateHaircut(long id, HaircutRequestDTO dto){
        Haircut haircut = haircutRepo.findById(id)
        .orElseThrow(()-> new ResourceNotFoundException("Haircut not found"));
        haircut.setType(dto.getType());
        haircut.setDescription(dto.getDescription());
        haircut.setPrice(dto.getPrice());
        haircut.setDuration(dto.getDuration());
        haircutRepo.save(haircut);
        return mapper.toDto(haircut);
    }

    @Override
    public List<HaircutResponseDTO> getAllHaircut(){
        List<HaircutResponseDTO> haircuts = haircutRepo.findAll()
        .stream()
        .map(haircut->mapper.toDto(haircut))
        .collect(Collectors.toList());
        return haircuts;
    }

    @Override
    public void deleteHaircut(long id){
        haircutRepo.deleteById(id);
        return;
    }


}

/*public HaircutResponseDTO createHaircut(HaircutResponseDTO dto);
    public HaircutResponseDTO updateHaircut(long id, HaircutRequestDTO dto);
    public void deleteHaircut(long id); */