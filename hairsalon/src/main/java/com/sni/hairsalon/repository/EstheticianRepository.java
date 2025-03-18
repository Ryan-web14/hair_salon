package com.sni.hairsalon.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sni.hairsalon.model.Esthetic;
import com.sni.hairsalon.model.Esthetician;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;

@Repository
public interface EstheticianRepository extends JpaRepository<Esthetician, Long> {
    Optional<Esthetician> findEstheticianById(long id);
    Esthetician findEstheticianByLastname(String lastname);
    List<Esthetician> findEstheticianByFirstname(String firstname);
    Esthetician findEstheticianByPhoneNumber(int phoneNumber);
    

    @Query("SELECT e FROM Esthetic e WHERE REPLACE(LOWER(e.type), ' ', '') = REPLACE(LOWER(:type), ' ', '')")
Optional<Esthetic> findEstheticByType(String type);

@Query("SELECT e FROM Esthetician e WHERE e.user.email = :email")
    Optional<Esthetician> findByEmail(String email);
    
    @Override
    @NonNull
    List<Esthetician> findAll();

    @Query("SELECT e FROM Esthetician e WHERE e.user.id = :userId")
    Optional<Esthetician> findByUserId(long userId);

    List<Esthetician> findByAvailable(boolean available);
    
    @Query("""
        SELECT DISTINCT e FROM Esthetician e 
        JOIN Schedule s ON s.esthetician.id = e.id 
        JOIN Availability a ON a.esthetician.id = e.id 
        WHERE s.dayOfWeek = :dayOfWeek 
        AND s.startTime <= :time 
        AND s.endTime >= :time 
        AND s.is_recurring = true 
        AND s.effectiveFrom <= :date 
        AND s.effectiveTo >= :date 
        AND a.isAvailable = true 
        AND a.startTime <= :time 
        AND a.endTime >= :time
    """)
    List<Esthetician> findAllAvailableEstheticians(
        @Param("dayOfWeek") DayOfWeek dayOfWeek,
        @Param("time") LocalDateTime time,
        @Param("date") Date date
    );

    @Query("""
        SELECT e FROM Esthetician e 
        JOIN Schedule s ON s.esthetician.id = e.id 
        JOIN Availability a ON a.esthetician.id = e.id 
        WHERE e.id = :estheticianId 
        AND s.dayOfWeek = :dayOfWeek 
        AND s.startTime <= :time 
        AND s.endTime >= :time 
        AND s.is_recurring = true 
        AND s.effectiveFrom <= :date 
        AND s.effectiveTo >= :date 
        AND a.isAvailable = true 
        AND a.startTime <= :time 
        AND a.endTime >= :time
    """)
    Optional<Esthetician> findAvailableEsthetician(
        @Param("estheticianId") Long estheticianId,
        @Param("dayOfWeek") DayOfWeek dayOfWeek,
        @Param("time") LocalDateTime time,
        @Param("date") Date date
    );

    @Query("""
        SELECT e FROM Esthetician e 
        JOIN Schedule s ON s.esthetician.id = e.id 
        JOIN Availability a ON a.esthetician.id = e.id 
        WHERE e.id = :estheticianId 
        AND s.dayOfWeek = :dayOfWeek 
        AND s.startTime <= :endTime 
        AND s.endTime >= :startTime 
        AND s.is_recurring = true 
        AND s.effectiveFrom <= :date 
        AND s.effectiveTo >= :date 
        AND a.isAvailable = true 
        AND a.startTime <= :endTime 
        AND a.endTime >= :startTime
    """)
    Optional<Esthetician> findEstheticianAvailableInTimeRange(
        @Param("estheticianId") Long estheticianId,
        @Param("dayOfWeek") DayOfWeek dayOfWeek,
        @Param("startTime") LocalDateTime startTime,
        @Param("endTime") LocalDateTime endTime,
        @Param("date") Date date
    );
}