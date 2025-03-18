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

import com.sni.hairsalon.dto.request.EstheticianRequestDTO;
import com.sni.hairsalon.dto.response.EstheticianResponseDTO;
import com.sni.hairsalon.service.EstheticianService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/v1/esthetician")
@RequiredArgsConstructor
public class EstheticianController {
    
    private final EstheticianService estheticianService;

    @PostMapping("/")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<EstheticianResponseDTO> createEstheticianByAdmin(@RequestBody EstheticianRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(estheticianService.createEstheticianByAdmin(request));
    }

    @GetMapping("/all")
    public ResponseEntity<List<EstheticianResponseDTO>> getAllEstheticians() {
        return ResponseEntity.ok().body(estheticianService.getAllEstheticians());
    }

    @GetMapping("/available")
    public ResponseEntity<List<EstheticianResponseDTO>> getAllAvailableEstheticians() {
        return ResponseEntity.ok().body(estheticianService.getAllAvailableEstheticians());
    }

    @PutMapping("/{id}/admin/update")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<EstheticianResponseDTO> updateEstheticianByAdmin(@PathVariable Long id, @RequestBody EstheticianRequestDTO request) {
        return ResponseEntity.ok().body(estheticianService.updateEsthetician(id, request));
    }

    @DeleteMapping("/{estheticianId}/admin/delete")
    public ResponseEntity<Void> deleteEsthetician(@PathVariable Long estheticianId) {
        estheticianService.deleteEsthetician(estheticianId);
        return ResponseEntity.noContent().build();
    }
}