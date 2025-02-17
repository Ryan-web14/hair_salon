package com.sni.hairsalon.service.serviceImpl;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sni.hairsalon.dto.request.ClientRequestDTO;
import com.sni.hairsalon.dto.request.ClientSignupRequest;
import com.sni.hairsalon.dto.request.UserRequestDTO;    
import com.sni.hairsalon.dto.response.UserResponseDTO;    
import com.sni.hairsalon.dto.response.AuthResponse;
import com.sni.hairsalon.dto.response.ClientResponseDTO;
import com.sni.hairsalon.dto.response.ClientSignupResponse;
import com.sni.hairsalon.dto.response.SessionResponseDTO;
import com.sni.hairsalon.exception.ResourceNotFoundException;
import com.sni.hairsalon.mapper.UserMapper;
import com.sni.hairsalon.model.User;
import com.sni.hairsalon.repository.UserRepository;
import com.sni.hairsalon.service.AuthentificationService;
import com.sni.hairsalon.utils.ValidationUtils;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthentificationServiceImpl implements AuthentificationService{
    
    private final UserRepository userRepo;
    private final SessionServiceImpl sessionService;
    private final UserServiceImpl userService;
    public final ClientServiceImpl clientService;
    private final AuthenticationManager authenticationManager;
    private final UserMapper mapper;

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
        return new AuthResponse(createdSession.getToken(), userDto.getEmail());
    }

    @Override
    @Transactional
    public ClientSignupResponse signupClient(ClientSignupRequest request){

        if(!ValidationUtils.isLetter(request.getFirstname()) || 
        !ValidationUtils.isAlphaWithSpaces(request.getFirstname())){
            throw new RuntimeException("Invalid firstname");
        }

        if(!ValidationUtils.isLetter(request.getLastname()) || 
        !ValidationUtils.isAlphaWithSpaces(request.getLastname())){
            throw new RuntimeException("Invalid Lastname");
        }

        if(!ValidationUtils.isValidEmail(request.getEmail())){
            throw new RuntimeException("Invalid email");
        }

        /*if(!ValidationUtils.isValidPhone(request.getPhone())){
            throw new RuntimeException("Invalid phone number");
        }*/

        UserRequestDTO dto = UserRequestDTO.builder()
        .email(request.getEmail())
        .role("CLIENT")
        .password(request.getPassword())
        .build();

        UserResponseDTO userDto = userService.createUser(dto);

        ClientRequestDTO clientDto = ClientRequestDTO.builder()
        .email(userDto.getEmail())
        .firstname(request.getFirstname())
        .lastname(request.getLastname())
        .phone(Integer.parseInt(request.getPhone()))
        .noShowCount(0)
        .build();

       ClientResponseDTO clientResponse =  clientService.createClient(clientDto);

       if(clientResponse.getEmail().isEmpty()){
            userRepo.deleteById(Long.parseLong(userDto.getId()));
       }
        return new ClientSignupResponse(clientResponse.getEmail());
    }  

    @Override
    public UserResponseDTO signupAdmin(UserRequestDTO request){
             
        if(!request.getRole().isEmpty()){
            request.setRole("");
        }

        return userService.createAdmin(request); 
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
