package com.sni.hairsalon.security.service;

import java.util.Collection;
import java.util.Collections;
import java.util.List;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import com.sni.hairsalon.exception.ResourceNotFoundException;
import com.sni.hairsalon.model.User;
import com.sni.hairsalon.repository.UserRepository;

@Component
@Validated
public class MyUserDetailsService implements UserDetailsService{
    
     private static final Logger logger = LoggerFactory.getLogger(MyUserDetailsService.class);

    @Autowired
    UserRepository userRepo;

    @Override
    public UserDetails loadUserByUsername(String email) {
        logger.debug("Trying to load user with email {}", email);
    try{
        User userResponse = userRepo.findUserByEmail(email)
        .orElseThrow(()->new ResourceNotFoundException("User not found with"));
        List<SimpleGrantedAuthority> authority = convertRolesToAuthorities(userResponse.getRole().getName());
        logger.info("User successfully authenticated: {}", email);
        logger.debug("Granted authorities: {}", authority);
        
        return new org.springframework.security.core.userdetails.User(
            email,
            userResponse.getPasswordHash(),
            authority);
    }catch(Exception e){
        logger.error("Error during user authentication for email: {}", email, e);
        throw e;
    }
        
    }

    private List<SimpleGrantedAuthority> convertRolesToAuthorities(String role){
        return Collections.singletonList(new SimpleGrantedAuthority(role));
    }
}
