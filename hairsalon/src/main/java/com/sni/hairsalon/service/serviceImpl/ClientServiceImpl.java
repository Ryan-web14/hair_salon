package com.sni.hairsalon.service.serviceImpl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.sni.hairsalon.dto.request.ClientRequestDTO;
import com.sni.hairsalon.dto.response.ClientResponseDTO;
import com.sni.hairsalon.exception.ResourceNotFoundException;
import com.sni.hairsalon.mapper.ClientMapper;
import com.sni.hairsalon.model.Client;
import com.sni.hairsalon.model.User;
import com.sni.hairsalon.repository.ClientRepository;
import com.sni.hairsalon.repository.UserRepository;
import com.sni.hairsalon.service.ClientService;
import com.sni.hairsalon.utils.ValidationUtils;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {
    
    private final EmailServiceImpl mailService;
    private final UserRepository userRepo;
   // private final UserServiceImpl userService;
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
        .phoneNumber(Integer.parseInt(dto.getPhone()))
        .noShowCount(0)
        .build();
        clientRepo.save(client);
        mailService.sendWelcomeMessage(client);
        return mapper.toDto(client);
    }

    @Override
    @Transactional
    public ClientResponseDTO updateClient(String email,ClientRequestDTO dto){

        validateField(dto);
        User user = userRepo.findUserByEmail(email)
        .orElseThrow(()->new ResourceNotFoundException("User not found"));
        Long userId = user.getId();

        Client client = clientRepo.findClientByUserID(userId)
        .orElseThrow(()-> new ResourceNotFoundException("Client not found"));
    
        client.setFirstname(dto.getFirstname());
        client.setLastname(dto.getLastname());
        client.setPhoneNumber(Integer.parseInt(dto.getPhone()));
        clientRepo.save(client);

        return mapper.toDto(client);
    }

    @Override
    @Transactional
    public void  deleteClient(long id){

        clientRepo.deleteById(id); 
        return;
    }

    @Override
    public ClientResponseDTO getClientById(long id){

        Client client = clientRepo.findById(id)
        .orElseThrow(()-> new ResourceNotFoundException("Client not found"));

        return mapper.toDto(client);
    }

    @Override
    public ClientResponseDTO getClientByEmail(String email){

        if(!ValidationUtils.isValidEmail(email)){
            throw new IllegalArgumentException("invalid email format");
        }

        Client client = clientRepo.findByEmail(email)
        .orElseThrow(()-> new ResourceNotFoundException("Client not found"));

        return mapper.toDto(client);
    }    

    @Override
    public List<ClientResponseDTO> searchClient(String lastname, String firstname){

        List<Client> clients = clientRepo.findByFirstAndLastname(lastname, firstname)
        .orElseThrow(()-> new ResourceNotFoundException("No user found with this name"));

        return clients
        .stream()
        .map(client-> mapper.toDto(client))
        .collect(Collectors.toList());
    }
    
    @Override
    public List<ClientResponseDTO> getAllClient(){

        return clientRepo.findAll()
        .stream()
        .map(client->mapper.toDto(client))
        .collect(Collectors.toList());
    }

    @Override
    public ClientResponseDTO getClientProfile(String email) {
        if (!ValidationUtils.isValidEmail(email)) {
            throw new IllegalArgumentException("Invalid email format");
        }

        Client client = clientRepo.findByEmail(email)
            .orElseThrow(() -> new ResourceNotFoundException("Client not found"));

        return mapper.toDto(client);
    }


      private void validateField(ClientRequestDTO clientRequest){
        if(clientRequest.getFirstname().isEmpty() || clientRequest.getLastname().isEmpty()){
            throw new RuntimeException("Empty name");
        }   
    
    
        if(!ValidationUtils.isLetter(clientRequest.getFirstname()) || ValidationUtils.isLetter(clientRequest.getLastname())){
            throw new RuntimeException("Names are invalid");
          
        }
       
       /*String phone = Integer.toString(clientRequest.getPhone()); 
        boolean verifiedPhone = ValidationUtils.isValidPhone(phone);
        if(!verifiedPhone || phone == null){
             throw new RuntimeException("Invalid phone number"+ phone);
            
    }8*/

 }

}

