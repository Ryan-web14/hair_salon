package com.sni.hairsalon.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sni.hairsalon.service.UserService;

import lombok.RequiredArgsConstructor;
@RestController
@RequestMapping("/v1/user")
@RequiredArgsConstructor
public class UserController {
  
    private final UserService userService;

    @DeleteMapping("/delete")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<Void> deleteAllUser(){
        
        userService.deleteAllUser();
        return ResponseEntity.noContent().build();

    }

}
