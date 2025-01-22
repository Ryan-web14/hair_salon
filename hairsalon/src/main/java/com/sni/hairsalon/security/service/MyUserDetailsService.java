package com.sni.hairsalon.security.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;


import com.sni.hairsalon.exception.ResourceNotFoundException;
import com.sni.hairsalon.model.User;
import com.sni.hairsalon.model.UserPrincipal;
import com.sni.hairsalon.repository.UserRepository;


@Service
public class MyUserDetailsService implements UserDetailsService{
    
     private static final Logger logger = LoggerFactory.getLogger(MyUserDetailsService.class);
     private final UserRepository userRepo;
     
    public MyUserDetailsService(UserRepository userRepo) {
        this.userRepo = userRepo;
    }


    @Override
    public UserDetails loadUserByUsername(String email) {
        logger.debug("Trying to load user with email {}", email);
    try{
        User userResponse = userRepo.findUserByEmail(email)
        .orElseThrow(()->new ResourceNotFoundException("User not found with"));
        logger.info("User successfully authenticated: {}", email);
        return new UserPrincipal(userResponse);
    }catch(Exception e){
        logger.error("Error during user authentication for email: {}", email, e);
        throw e;
    }
        
    }
}
