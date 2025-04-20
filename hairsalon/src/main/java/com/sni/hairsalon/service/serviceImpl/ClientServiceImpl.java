package com.sni.hairsalon.service.serviceImpl;

import java.time.LocalDate;
import java.time.Year;
import java.time.YearMonth;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
import com.sni.hairsalon.service.EmailService;
import com.sni.hairsalon.utils.ValidationUtils;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {
    
    private final EmailService mailService;
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
        .lastname(dto.getLastname().toUpperCase())
        .firstname(dto.getFirstname())
        .phoneNumber(Integer.parseInt(dto.getPhone()  ))
        .noShowCount(0)
        .build();
        clientRepo.save(client);
        mailService.sendWelcomeMessage(client);
        return mapper.toDto(client);
    }

    @Override
    @Transactional
    public ClientResponseDTO updateClient(String email, ClientRequestDTO dto){

        validateField(dto);
        User user = userRepo.findUserByEmail(email)
        .orElseThrow(()->new ResourceNotFoundException("User not found"));
        Long userId = user.getId();

        Client client = clientRepo.findClientByUserID(userId)
        .orElseThrow(()-> new ResourceNotFoundException("Client not found"));
      
        client.setFirstname(dto.getFirstname());
        client.setLastname(dto.getLastname().toUpperCase());
        client.setPhoneNumber(Integer.parseInt(dto.getPhone()));
        client.setNoShowCount(dto.getNoShowCount());
        clientRepo.save(client);

        return mapper.toDto(client);
    }

    @Override
    @Transactional
    public ClientResponseDTO updateAdminClient(long clientId, ClientRequestDTO dto){

        validateField(dto);
        Client client = clientRepo.findById(clientId)
        .orElseThrow(()-> new ResourceNotFoundException("Client not found"));
      
        client.setFirstname(dto.getFirstname());
        client.setLastname(dto.getLastname().toUpperCase());
        client.setPhoneNumber(Integer.parseInt(dto.getPhone()));
        client.setNoShowCount(dto.getNoShowCount());
        clientRepo.save(client);

        return mapper.toDto(client);
    }

    @Override
    @Transactional
    public void  deleteClient(long id){
        Client client = clientRepo.findById(id)
        .orElseThrow(()->new ResourceNotFoundException("No client found"));
        clientRepo.deleteById(client.getId()); 
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

    @Override
    public void deleteAllClient(){

        clientRepo.deleteAll();
        return;
    }


      private void validateField(ClientRequestDTO clientRequest){
        if(clientRequest.getFirstname().isEmpty() || clientRequest.getLastname().isEmpty()){
            throw new RuntimeException("Empty name");
        }   
    
    
        if(!ValidationUtils.isLetter(clientRequest.getFirstname()) || !ValidationUtils.isLetter(clientRequest.getLastname())){
            throw new RuntimeException("Names are invalid");
          
        }
       
       String phone = clientRequest.getPhone(); 
        boolean verifiedPhone = ValidationUtils.isValidPhone(phone);
        if(!verifiedPhone || phone == null){
             throw new RuntimeException("Invalid phone number"+ phone);
            
    }

 }

    @Transactional
    public List<ClientResponseDTO> getUniqueClientsWithAppointment() {

        LocalDate startOfMonth = YearMonth.now().atDay(1);
        LocalDate endOfMonth = YearMonth.now().atEndOfMonth();
        return clientRepo.findUniqueClientWithAppointment(startOfMonth, endOfMonth)
        .stream()
        .map(client->mapper.toDto(client))
        .collect(Collectors.toList()); 
    }

    @Transactional
    public Page<ClientResponseDTO> getPaginatedClient(Pageable page){

        Page<Client> paginatedClient =  clientRepo.findAll(page);

        Page<ClientResponseDTO> clientPageResponse = paginatedClient.map(mapper::toDto);

        return clientPageResponse;

    }

}

