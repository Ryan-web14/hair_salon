package com.sni.hairsalon.service.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

public class UserServiceImpl implements UserService{

    @Autowired
    private UserRole role; 
    
    @Autowired
    private UserRoleRepository roleRepo;

    @Autowired
    private UserRepository userRepo;

    UserMapper userMapper;

    @Override
    public UserResponseDTO createUser(UserRequestDTO dto){
        User newUser = new User();
        if(!EmailAlreadyExist(dto.getEmail())){
            throw new ResourceAlreadyExistException("user already exist");
        }
        UserRole role = roleRepo.findUserRoleByName(dto.getRole());
        newUser.setEmail(dto.getEmail());
        newUser.setRole(role);
        newUser.setPasswordHash(dto.getPassword());
        UserResponseDTO dtoResponse = UserResponseDTO.create(newUser.getId(), newUser.getRole().getName(),newUser.getEmail());
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
     public void deleteUser(long id){
        userRepo.deleteUser(id);
     }

     @Override
     public List<UserResponseDTO> getAllUsers(){
        List<User> users = userRepo.findAll();
        List<UserResponseDTO> userResponses = new ArrayList<>();
        for(User user : users){
            UserResponseDTO userResponseDTO = userMapper.toDto(user);
            userResponses.add(userResponseDTO);
        }
        return userResponses;
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
        user.setPasswordHash(newPassword);
        return userMapper.toDto(user);
    }

    private boolean EmailAlreadyExist(String email){
        User userAlreadyExist = userRepo.findUserByEmail(email)
        .orElseThrow(()-> new ResourceNotFoundException("user not found with email: " + email));
        
        if(userAlreadyExist.getEmail() != null){
            return true;
        }
        return false;
    } 
}
