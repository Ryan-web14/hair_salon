package com.sni.hairsalon.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import com.sni.hairsalon.model.User;


@Repository
public interface UserRepository extends JpaRepository<User, Long>{
    Optional <User> findUserById(long userId);
    User findById(long id);
    Optional <User> findUserByEmail(String email);
    void deleteById(long id);

    @Override
    @NonNull
    List<User> findAll();
}
