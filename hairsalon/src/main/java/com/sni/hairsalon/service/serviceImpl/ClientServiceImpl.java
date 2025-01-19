package com.sni.hairsalon.service.serviceImpl;

import org.springframework.stereotype.Service;

import com.sni.hairsalon.dto.request.ClientRequestDTO;
import com.sni.hairsalon.dto.response.ClientResponseDTO;
import com.sni.hairsalon.dto.response.UserResponseDTO;
import com.sni.hairsalon.exception.ResourceNotFoundException;
import com.sni.hairsalon.mapper.ClientMapper;
import com.sni.hairsalon.model.Client;
import com.sni.hairsalon.model.User;
import com.sni.hairsalon.repository.ClientRepository;
import com.sni.hairsalon.repository.UserRepository;
import com.sni.hairsalon.service.ClientService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {
    
    private final UserRepository userRepo;
    private final ClientRepository clientRepo;
    private final ClientMapper mapper;

    @Override
    @Transactional
    public ClientResponseDTO createClient(ClientRequestDTO dto){

        User user = userRepo.findUserByEmail(dto.getEmail())
        .orElseThrow(()-> new ResourceNotFoundException("User not found"));

        Client client = Client.builder()
        .user(user)
        .lastname(dto.getLastname())
        .firstname(dto.getFirstname())
        .phoneNumber(dto.getPhone())
        .noShowCount(0)
        .build();
        clientRepo.save(client);
        return mapper.toDto(client);

    }

    

}
