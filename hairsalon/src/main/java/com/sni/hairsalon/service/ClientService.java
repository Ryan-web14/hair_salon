package com.sni.hairsalon.service;

import com.sni.hairsalon.dto.request.ClientRequestDTO;
import com.sni.hairsalon.dto.response.ClientResponseDTO;

import java.util.List;

public interface ClientService {
    
    public ClientResponseDTO createClient(ClientRequestDTO dto);
    public ClientResponseDTO updateClient(String email,ClientRequestDTO dto);
    public void deleteClient(long id);
    public ClientResponseDTO getClientById(long id);
    public ClientResponseDTO getClientByEmail(String email);
    public List<ClientResponseDTO> getAllClient();
    public List<ClientResponseDTO> searchClient(String lastname, String firstname);
    public ClientResponseDTO getClientProfile(String email);  
    public ClientResponseDTO updateAdminClient(long clientId, ClientRequestDTO dto);
    public void deleteAllClient();

    
/* 

 need to work with email sending for this
->updateClientPassword
->updateClientEmailPAssword 
*/
}
