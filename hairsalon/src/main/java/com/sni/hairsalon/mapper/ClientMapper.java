package com.sni.hairsalon.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sni.hairsalon.dto.request.ClientRequestDTO;
import com.sni.hairsalon.dto.response.ClientResponseDTO;
import com.sni.hairsalon.exception.ResourceNotFoundException;
import com.sni.hairsalon.model.Client;
import com.sni.hairsalon.model.User;
import com.sni.hairsalon.repository.ClientRepository;
import com.sni.hairsalon.repository.UserRepository;
import com.sni.hairsalon.utils.ValidationUtils;

@Component
public class ClientMapper{

    UserRepository userRepo;

    @Autowired
    ClientRepository clientRepo;
    
    public ClientResponseDTO toDto(Client client){
        ClientResponseDTO dto = new ClientResponseDTO();
        dto.setId(String.valueOf(client.getId()));
        dto.setEmail(client.getUser().getEmail());
        dto.setFirstname(client.getFirstname());
        dto.setLastname(client.getLastname());
        dto.setPhone("0" + String.valueOf(client.getPhoneNumber()));
        dto.setNoShowCount(client.getNoShowCount());
        return dto;
    }

    public Client toEntity(ClientRequestDTO request){
        validateField(request);
        User user = userRepo.findUserByEmail(request.getEmail())
        .orElseThrow(()-> new ResourceNotFoundException("Client not found"));
        Client newClient = new Client();
        newClient.setFirstname(request.getFirstname());
        newClient.setLastname(request.getLastname());
        newClient.setPhoneNumber(request.getPhone());
        newClient.setUser(user);
        return newClient;
    
    }

    private void validateField(ClientRequestDTO clientRequest){
        if(clientRequest.getFirstname().isEmpty() || clientRequest.getLastname().isEmpty()){
            throw new RuntimeException("Empty name");
        }   
    
    
        if(!ValidationUtils.isLetter(clientRequest.getFirstname()) || ValidationUtils.isLetter(clientRequest.getLastname())){
            throw new RuntimeException("Names are invalid");
          
        }
       
        String phone = Integer.toString(clientRequest.getPhone()); 
        boolean verifiedPhone = ValidationUtils.isValidPhone(phone);
        if(!verifiedPhone || phone == null){
             throw new RuntimeException("Invalid phone number"+ phone);
            
    }
 }
}
