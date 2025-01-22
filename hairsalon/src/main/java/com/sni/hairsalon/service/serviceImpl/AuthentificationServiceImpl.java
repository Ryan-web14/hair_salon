package com.sni.hairsalon.service.serviceImpl;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.sni.hairsalon.dto.request.UserRequestDTO;    
import com.sni.hairsalon.dto.response.UserResponseDTO;    
import com.sni.hairsalon.dto.response.AuthResponse;
import com.sni.hairsalon.dto.response.SessionResponseDTO;
import com.sni.hairsalon.exception.ResourceNotFoundException;
import com.sni.hairsalon.mapper.UserMapper;
import com.sni.hairsalon.model.User;
import com.sni.hairsalon.repository.UserRepository;
import com.sni.hairsalon.service.AuthentificationService;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class AuthentificationServiceImpl implements AuthentificationService{
    
    private final UserRepository userRepo;
    private final SessionServiceImpl sessionService;
    private final UserServiceImpl userService;
    private AuthenticationManager authenticationManager;
    private UserMapper mapper;

    public AuthentificationServiceImpl(
            UserRepository userRepo, 
            SessionServiceImpl sessionService,
            UserServiceImpl userService,
            AuthenticationManager authenticationManager, 
            UserMapper mapper) {
        this.userRepo = userRepo;
        this.sessionService = sessionService;
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.mapper = mapper;
    }

    @Override 
    public AuthResponse login(UserRequestDTO dto, HttpServletRequest request){
        Authentication authentication =  authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken
            (dto.getEmail(),
             dto.getPassword()
             ));
         SecurityContextHolder.getContext().setAuthentication(authentication);
        User user =  userRepo.findUserByEmail(dto.getEmail())
        .orElseThrow(()-> new ResourceNotFoundException("User not found"));
        UserResponseDTO userDto = mapper.toDto(user);
        SessionResponseDTO createdSession = sessionService.createSession(request, userDto);
        return new AuthResponse(createdSession.getToken(), userDto.getEmail(),userDto.getRole());
    }

    

    @Override
    public void logout(String token) {
        sessionService.invalidateSession(token);
    }

    @Override
    public UserResponseDTO signUp(UserRequestDTO user){
       UserResponseDTO dto = userService.createUser(user);
       return dto;
    }
}
