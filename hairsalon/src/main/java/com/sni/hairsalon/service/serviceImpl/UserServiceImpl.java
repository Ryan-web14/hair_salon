package com.sni.hairsalon.service.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;
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

@Service
public class UserServiceImpl implements UserService{
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRoleRepository roleRepo;

    @Autowired
    private UserRepository userRepo;



    @Autowired
    UserMapper userMapper;

    @Override
    public UserResponseDTO createUser(UserRequestDTO dto){
        User newUser = new User();
        if(EmailAlreadyExist(dto.getEmail())){
            throw new ResourceAlreadyExistException("user already exist");
        }
        UserRole role = roleRepo.findUserRoleByName(dto.getRole())
        .orElseThrow(()-> new ResourceNotFoundException("Role not found"));
        newUser.setEmail(dto.getEmail());
        newUser.setRole(role);
        newUser.setPasswordHash(passwordEncoder.encode(dto.getPassword()));
        userRepo.save(newUser);
        UserResponseDTO dtoResponse = UserResponseDTO.create(newUser.getId(),
         newUser.getRole().getName(),newUser.getEmail());
        return dtoResponse;
    }

    @Override
    public UserResponseDTO getUserById(long id){
        User user = userRepo.findUserById(id)
        .orElseThrow(()->new ResourceNotFoundException("user not found"));
        UserResponseDTO dto = UserResponseDTO.create(user.getId(), user.getRole().getName(), user.getEmail());
        return dto;
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
     public UserResponseDTO updateUser(long id, UserRequestDTO dto){
        User user = userRepo.findUserById(id)
        .orElseThrow(()->new ResourceNotFoundException("user not found"));
        
        if(ValidationUtils.isValidEmail(dto.getEmail())){
            user.setEmail(dto.getEmail());
        }
        userRepo.save(user);
        return userMapper.toDto(user);
     }

    @Override
    public UserResponseDTO updatePassword(long id, String newPassword){
        User user = userRepo.findUserById(id)
        .orElseThrow(()->new ResourceNotFoundException("user not found")); 
        user.setPasswordHash(passwordEncoder.encode(newPassword));
        return userMapper.toDto(user);
    }

    private boolean EmailAlreadyExist(String email){
        return userRepo.findUserByEmail(email).isPresent();
    }
}