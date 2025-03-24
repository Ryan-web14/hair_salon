package com.sni.hairsalon.service.serviceImpl;

import java.nio.file.AccessDeniedException;
import java.nio.file.attribute.UserPrincipal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticatedPrincipal;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
import com.sni.hairsalon.security.service.RandomAlphaNumericGeneratorService;
import com.sni.hairsalon.service.AuthentificationService;
import com.sni.hairsalon.service.ClientService;
import com.sni.hairsalon.service.EmailService;
import com.sni.hairsalon.service.SessionService;
import com.sni.hairsalon.service.UserService;
import com.sni.hairsalon.utils.ValidationUtils;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthentificationServiceImpl implements AuthentificationService{
    
    private final UserRepository userRepo;
    private final SessionService sessionService;
    private final UserService userService;
    public final ClientService clientService;
    private final AuthenticationManager authenticationManager;
    private final UserMapper mapper;
    private final EmailService mailService;
    private final RandomAlphaNumericGeneratorService randomStringService;

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
        user.setLast_login(LocalDateTime.now().truncatedTo(ChronoUnit.MILLIS));
        userRepo.save(user);
        return new AuthResponse(createdSession.getToken(), userDto.getEmail());
    }

    @Override
    public AuthResponse loginAdmin(UserRequestDTO dto, HttpServletRequest request){
        
        User user =  userRepo.findUserByEmail(dto.getEmail())
        .orElseThrow(()-> new UsernameNotFoundException("No user found"));

        Boolean hasCorrectRole = user.getRole().getName().equals("ADMIN") ||
        user.getRole().getName().equals("MANAGER");

        if(!hasCorrectRole){

            try{
                throw new AccessDeniedException("UNAUTHORIZED");
            }catch(AccessDeniedException e){

                throw new RuntimeException("Access denied: " + e.getFile()); 
            }
               
        }

        Authentication authentication  = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                dto.getEmail(),
            dto.getPassword()
            ));     
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserResponseDTO userDto = mapper.toDto(user);
        SessionResponseDTO createdSession = sessionService.createSession(request, userDto);
        user.setLast_login(LocalDateTime.now().truncatedTo(ChronoUnit.MILLIS));
        userRepo.save(user);
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

        if(!ValidationUtils.isValidPhone(request.getPhone())){
            throw new RuntimeException("Invalid phone number");
        }

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
        .phone(request.getPhone())
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
        
        request.setRole("ADMIN");

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

    @Override
    public UserResponseDTO signupManager(UserRequestDTO request){
        if (!request.getRole().isEmpty()) {
            request.setRole("");
        }

        request.setRole("MANAGER");

        return userService.createAdmin(request);
    }

    @Override
    @Transactional
    public ClientSignupResponse signupClientByAdmin(ClientSignupRequest request, String link){

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

        if(!ValidationUtils.isValidPhone(request.getPhone())){
            throw new RuntimeException("Invalid phone number");
        }

        String randomPassword = randomStringService.generateRandomAlphaNumeric(10);

        UserRequestDTO dto = UserRequestDTO.builder()
        .email(request.getEmail())
        .role("CLIENT")
        .password(randomPassword)
        .build();

        UserResponseDTO userDto = userService.createUser(dto);

        ClientRequestDTO clientDto = ClientRequestDTO.builder()
        .email(userDto.getEmail())
        .firstname(request.getFirstname())
        .lastname(request.getLastname())
        .phone(request.getPhone())
        .noShowCount(0)
        .build();

        
       ClientResponseDTO clientResponse =  clientService.createClient(clientDto);

       mailService.sendTemporaryPasswordChangeEmail(request.getEmail(),randomPassword, link);

       if(clientResponse.getEmail().isEmpty()){
            userRepo.deleteById(Long.parseLong(userDto.getId()));
       }
        return new ClientSignupResponse(clientResponse.getEmail(), clientResponse.getFirstname(),
         clientResponse.getLastname(),clientResponse.getPhone());
    }


}
/*private String email;
    private String firstname;
    private String lastname;
    private String phone; */