package com.sni.hairsalon.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sni.hairsalon.dto.request.ClientRequestDTO;
import com.sni.hairsalon.dto.response.ClientResponseDTO;
import com.sni.hairsalon.dto.response.PaginatedResponse;
import com.sni.hairsalon.model.Client;
import com.sni.hairsalon.model.UserPrincipal;
import com.sni.hairsalon.service.ClientService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/v1/client")
@RequiredArgsConstructor
public class ClientController {
    
    private final ClientService clientService;

    @PostMapping("/")
    public ResponseEntity<ClientResponseDTO> createClient(
    @RequestBody ClientRequestDTO client){
        
        return ResponseEntity.status(HttpStatus.CREATED).
        body(clientService.createClient(client));

    }

    @PutMapping("/update")
    public ResponseEntity<ClientResponseDTO> updateClient(@AuthenticationPrincipal UserPrincipal authenticatedUser,
    @RequestBody ClientRequestDTO request){
        String email = authenticatedUser.getUsername();
        return ResponseEntity.ok().body(clientService.updateClient(email, request));
    }

    @PutMapping("/{clientId}/admin/update")
    @PreAuthorize("hasRole('Admin') or hasRole('MANAGER')")
    public ResponseEntity<ClientResponseDTO> updateAdminClient(@PathVariable Long clientId, @RequestBody ClientRequestDTO request){

        return ResponseEntity.ok().body(clientService.updateAdminClient(clientId, request));
    }

    @GetMapping("/admin/all")
    @PreAuthorize( "hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity <List<ClientResponseDTO>> getAllClient(){
        
        return ResponseEntity.ok().body(clientService.getAllClient());
    }

    @GetMapping("/unique")
    @PreAuthorize( "hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<List<ClientResponseDTO>> getAllUniqueClient(){
        return ResponseEntity.ok().body(clientService.getUniqueClientsWithAppointment());
    }



    @GetMapping("/admin/all/paginated")
    @PreAuthorize( "hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<PaginatedResponse<ClientResponseDTO>> getAllPaginatedClient(){

        int page = 0;
        int size = 15;
        Pageable pageable = PageRequest.of(page, size);
        Page<ClientResponseDTO> clientPage = clientService.getPaginatedClient(pageable); 
        
        List<ClientResponseDTO> content = clientPage.getContent();
         
        Map<String, Object> pagination = new HashMap<>();
        pagination.put("totalItems", clientPage.getTotalElements());
        pagination.put("totalPages", clientPage.getTotalPages());
        pagination.put("currentPage", clientPage.getNumber());
        pagination.put("itemsPerPage", clientPage.getSize());
        pagination.put("hasNextPage", clientPage.hasNext());
        pagination.put("hasPrevPage", clientPage.hasPrevious());
        
        PaginatedResponse<ClientResponseDTO> response = new PaginatedResponse<>(content, pagination);
        
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{clientId}/delete")
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<Void> deleteClient(@PathVariable long clientId){

        clientService.deleteClient(clientId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/delete")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<Void> deleteAllAvailability(){
        clientService.deleteAllClient();;
        return ResponseEntity.noContent().build();
    }




}
