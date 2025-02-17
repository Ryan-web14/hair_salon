package com.sni.hairsalon.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import com.sni.hairsalon.model.ClientHistoric;

@Repository
public interface ClientHistoricRepository extends JpaRepository<ClientHistoric,Long> {
    @Override
    @NonNull
    List<ClientHistoric> findAll();
    
    ClientHistoricRepository findClientHistoricById(long id);
    ClientHistoric findByClientId(Long clientId);

    
}
