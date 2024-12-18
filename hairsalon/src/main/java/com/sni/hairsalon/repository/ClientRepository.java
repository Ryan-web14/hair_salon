package com.sni.hairsalon.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import com.sni.hairsalon.model.Client;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long>{
    Client findById(long id);
    Client findClientByFirstname(String name);
    Client findClientByLastname(String name);
    Client findClientByPhoneNumber(int phone);
    
    @NativeQuery
    @Query("Select c from Client c where c.user.id = :userId")
    Client findClientByUserID(@Param("userId") long userId);

    @Override
    @NonNull
    List<Client> findAll();
    

}
