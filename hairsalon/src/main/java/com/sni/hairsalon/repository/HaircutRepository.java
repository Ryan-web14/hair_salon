package com.sni.hairsalon.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import com.sni.hairsalon.model.Haircut;

@Repository
public interface HaircutRepository extends JpaRepository<Haircut,Long>{
    Optional<Haircut> findHaircutById(long id);


    @Query("SELECT h FROM Haircut h WHERE REPLACE(LOWER(h.type), ' ', '') = REPLACE(LOWER(:type), ' ', '')")
    Optional<Haircut> findHaircutByType(String type); 
    
    List<Haircut> findHaircutByDuration(int duration);
    List<Haircut> findHaircutByPrice(int price);
    Void deleteById(long id);
    @Override
    @NonNull
    List<Haircut> findAll();
}
