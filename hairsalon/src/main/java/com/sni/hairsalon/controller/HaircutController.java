package com.sni.hairsalon.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sni.hairsalon.dto.request.HaircutRequestDTO;
import com.sni.hairsalon.dto.response.HaircutResponseDTO;
import com.sni.hairsalon.model.Haircut;
import com.sni.hairsalon.repository.HaircutRepository;
import com.sni.hairsalon.service.HaircutService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/v1/haircut")
@RequiredArgsConstructor
public class HaircutController {
    
    private final HaircutService haircutService;
    private final HaircutRepository repo;

    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<HaircutResponseDTO> createHaircut(@RequestBody HaircutRequestDTO dto){
        return ResponseEntity.status(HttpStatus.CREATED)
        .body(haircutService.createHaircut(dto));
    }

    @PutMapping("/{id}/update")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<HaircutResponseDTO> updateHaircut(@PathVariable Long id, @RequestBody HaircutRequestDTO dto){
        return ResponseEntity.ok(haircutService.updateHaircut(id, dto));
    }

    @GetMapping("/all")
    public ResponseEntity<List<HaircutResponseDTO>> getAllHaircuts(){
        return ResponseEntity.ok(haircutService.getAllHaircut());
    }
    
    @DeleteMapping("/{id}/delete")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteHaircut(@PathVariable Long id){
        haircutService.deleteHaircut(id);
        return ResponseEntity.noContent().build();
    }
}
