package com.sni.hairsalon.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import com.sni.hairsalon.model.Haircut;

@Repository
public interface HaircutRepository extends JpaRepository<Haircut,Long>{
    Haircut findHaircutById(long id);
    Haircut findHaircutByType(String type);
    List<Haircut> findHaircutByDuration(int duration);
    List<Haircut> findHaircutByPrice(int price);

    @Override
    @NonNull
    List<Haircut> findAll();
}