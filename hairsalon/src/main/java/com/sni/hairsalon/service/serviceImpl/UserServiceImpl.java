package com.sni.hairsalon.service.serviceImpl;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.sni.hairsalon.dto.request.UserRequestDTO;
import com.sni.hairsalon.dto.response.UserResponseDTO;
import com.sni.hairsalon.exception.ResourceAlreadyExistException;
import com.sni.hairsalon.exception.ResourceNotFoundException;
import com.sni.hairsalon.mapper.UserMapper;
import com.sni.hairsalon.model.User;
import com.sni.hairsalon.model.UserRole;
import com.sni.hairsalon.repository.UserRepository;
import com.sni.hairsalon.repository.UserRoleRepository;
import com.sni.hairsalon.service.UserService;
import com.sni.hairsalon.utils.ValidationUtils;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{
    
    private final PasswordEncoder passwordEncoder;
    private final UserRoleRepository roleRepo;
    private final UserRepository userRepo;    
    private final UserMapper userMapper;

    @Override
    @Transactional
    public UserResponseDTO createUser(UserRequestDTO dto){
        User newUser = new User();
        if(emailAlreadyExist(dto.getEmail())){
            throw new ResourceAlreadyExistException("user already exist");
        }
        UserRole role = roleRepo.findUserRoleByName(dto.getRole())
        .orElseThrow(()-> new ResourceNotFoundException("Role not found"));
        newUser.setEmail(dto.getEmail());
        newUser.setRole(role);
        newUser.setPasswordHash(passwordEncoder.encode(dto.getPassword()));
        userRepo.save(newUser);
        
        return userMapper.toDto(newUser);
    }

    @Override
    @Transactional
    public UserResponseDTO createAdmin(UserRequestDTO dto){
        
        if(emailAlreadyExist(dto.getEmail())){
            throw new ResourceAlreadyExistException("user already exist");
        }
        
        if(!dto.getRole().isEmpty()){
            dto.setRole("");
        }

        UserRole role = roleRepo.findUserRoleByName("ADMIN")
        .orElseThrow(()-> new ResourceNotFoundException("Role not found"));

        User newAdmin = User.builder()
        .email(dto.getEmail())
        .role(role)
        .passwordHash(passwordEncoder.encode(dto.getPassword()))
        .build();
        userRepo.save(newAdmin);

        return userMapper.toDto(newAdmin);

    

    }

    @Override
    public UserResponseDTO getUserById(long id){
        User user = userRepo.findUserById(id)
        .orElseThrow(()->new ResourceNotFoundException("user not found"));
        return userMapper.toDto(user);
     }

     @Override
     public List<UserResponseDTO> getAllUsers(){
        List<UserResponseDTO> users = userRepo.findAll()
        .stream()
        .map(user -> userMapper.toDto(user))
        .collect(Collectors.toList());
        return users;
     }

     @Override
     public UserResponseDTO updateUser(long id, String email){
        User user = userRepo.findUserById(id)
        .orElseThrow(()->new ResourceNotFoundException("user not found"));
        
        if(ValidationUtils.isValidEmail(email)){
            user.setEmail(email);
        }
        userRepo.save(user);
        return userMapper.toDto(user);
     }

     @Override
     public void updateLastLogin(long id){
        User user = userRepo.findUserById(id)
            .orElseThrow(() -> new ResourceNotFoundException("user not found"));
        user.setLast_login(LocalDateTime.now());    
        userRepo.save(user);
     }

    @Override
    public UserResponseDTO updatePassword(long id, String newPassword){
        User user = userRepo.findUserById(id)
        .orElseThrow(()->new ResourceNotFoundException("user not found")); 
        user.setPasswordHash(passwordEncoder.encode(newPassword));
        userRepo.save(user);
        return userMapper.toDto(user);
    }
    
    public User getUserByEmail(String email){

        User user = userRepo.findUserByEmail(email)
        .orElseThrow(()-> new ResourceNotFoundException("User not found"));

        return user;
    }

    @Override
    public boolean emailAlreadyExist(String email){
        return userRepo.findUserByEmail(email).isPresent();
    }
}