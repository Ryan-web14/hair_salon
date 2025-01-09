package com.sni.hairsalon.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.sni.hairsalon.model.Availability;


@Repository
public interface AvailabilityRepository extends JpaRepository<Availability,Long>{
    Availability findAvailabilityById(long id);
    List<Availability> findAvailabilityByStartTime(LocalDateTime startTime);
    List<Availability> findAvailabilityByEndTime(LocalDateTime endTime);
    List<Availability> findByBarberIdAndStartTimeBetweenAndIsAvailableTrue(
        Long barberId, 
        LocalDateTime startTime, 
        LocalDateTime endTime
    );
    @Query("select a from Availability a where a.barber.id = :baberId")
    List<Availability> findAvailabilityByBarberId(@Param("barberId") long barberId);


    
}
