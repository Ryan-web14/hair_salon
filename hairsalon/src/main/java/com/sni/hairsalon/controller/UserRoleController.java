package com.sni.hairsalon.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sni.hairsalon.dto.request.UserRoleRequestDTO;
import com.sni.hairsalon.dto.response.UserRoleResponseDTO;
import com.sni.hairsalon.service.UserRoleService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/v1/role")
@RequiredArgsConstructor
public class UserRoleController {

    private final UserRoleService roleService;

    @PostMapping("/")
    public ResponseEntity<UserRoleResponseDTO> createRole(@RequestBody UserRoleRequestDTO request){

        return ResponseEntity.ok(roleService.createRole(request));
    }
}
