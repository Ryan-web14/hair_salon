package com.sni.hairsalon.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import com.sni.hairsalon.model.User;

import jakarta.annotation.Nonnull;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{
    User findUserById(long userId);
    User findUserByEmail(String email);
    
    @Override
    @NonNull
    List<User> findAll();
}
