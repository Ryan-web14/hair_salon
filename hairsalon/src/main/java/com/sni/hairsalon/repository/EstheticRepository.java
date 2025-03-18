package com.sni.hairsalon.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sni.hairsalon.model.Esthetic;

import java.util.List;
import java.util.Optional;

@Repository
public interface EstheticRepository extends JpaRepository<Esthetic, Long> {
    
    Optional<Esthetic> findEstheticById(long id);
        Optional<Esthetic> findEstheticByType(String type);
        List<Esthetic> findEstheticByPriceLessThanEqual(int maxPrice);
        List<Esthetic> findEstheticByDurationLessThanEqual(int maxDuration);
        List<Esthetic> findEstheticByTypeContainingIgnoreCase(String keyword);
        List<Esthetic> findEstheticByDescriptionContainingIgnoreCase(String keyword);
        List<Esthetic> findEstheticByPriceLessThanEqualAndDurationLessThanEqual(int maxPrice, int maxDuration);
}