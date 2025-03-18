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
    
    @Query("SELECT a FROM Availability a WHERE a.barber.id = :barberId " +
    "AND a.startTime BETWEEN :startTime AND :endTime " + 
    "AND a.isAvailable = true")
List<Availability> findByBarberIdAndDateAndIsAvailableTrue(
 @Param("barberId") Long barberId,
 @Param("startTime") LocalDateTime startTime,
 @Param("endTime") LocalDateTime endTime
);

@Query("SELECT a FROM Availability a WHERE a.esthetician.id = :estheticianId " +
       "AND a.startTime BETWEEN :startTime AND :endTime " + 
       "AND a.isAvailable = true")
List<Availability> findByEstheticianIdAndDateAndIsAvailableTrue(
    @Param("estheticianId") Long estheticianId,
    @Param("startTime") LocalDateTime startTime,
    @Param("endTime") LocalDateTime endTime
);

    @Query("select a from Availability a where a.barber.id = :barberId")
    List<Availability> findAvailabilityByBarberId(@Param("barberId") long barberId);

    @Query("Select a from Availability a where a.barber.id = :barberId " +
    "and a.startTime = :startTime "+
    "and a.endTime = :endTime " +
    "and isAvailable = true")
    List<Availability> findByStartAndEndTimeAndBarber(
        @Param("barberId") long barberId,
        @Param("startTime") LocalDateTime starTime,
        @Param("endTime") LocalDateTime endTime);

        @Query("SELECT a FROM Availability a WHERE a.barber.id = :barberId " +
       "AND ((a.startTime < :endTime AND a.endTime > :startTime))")
List<Availability> findOverlappingSlots(
    @Param("barberId") long barberId, 
    @Param("startTime") LocalDateTime startTime, 
    @Param("endTime") LocalDateTime endTime);


    // Find slots that overlap with a given time range
    @Query("SELECT a FROM Availability a WHERE a.barber.id = :barberId " +
           "AND ((a.startTime <= :endTime AND a.endTime >= :startTime))")
    List<Availability> findByBarberAndTimeRangeOverlap(
        @Param("barberId") Long barberId, 
        @Param("startTime") LocalDateTime startTime, 
        @Param("endTime") LocalDateTime endTime);
        
    // Check if a specific slot exists
    @Query("SELECT COUNT(a) > 0 FROM Availability a " +
    "WHERE a.barber.id = :barberId " +
    "AND a.startTime = :startTime " +
    "AND a.endTime = :endTime")
boolean existsByBarberIdAndStartTimeAndEndTime(
 @Param("barberId") Long barberId, 
 @Param("startTime") LocalDateTime startTime, 
 @Param("endTime") LocalDateTime endTime
 );
 
 @Query("SELECT a FROM Availability a WHERE a.esthetician.id = :estheticianId " +
 "AND ((a.startTime <= :endTime AND a.endTime >= :startTime))")
List<Availability> findByEstheticianAndTimeRangeOverlap(
@Param("estheticianId") Long estheticianId, 
@Param("startTime") LocalDateTime startTime, 
@Param("endTime") LocalDateTime endTime);

// Check if a specific slot exists
@Query("SELECT COUNT(a) > 0 FROM Availability a " +
"WHERE a.esthetician.id = :estheticianId " +
"AND a.startTime = :startTime " +
"AND a.endTime = :endTime")
boolean existsByEstheticianIdAndStartTimeAndEndTime(
@Param("estheticianId") Long estheticianId, 
@Param("startTime") LocalDateTime startTime,
@Param("endTime") LocalDateTime endTime);
 @Query("Select a from Availability a where a.esthetician.id = :estheticianId " +
 "and a.startTime = :startTime "+
 "and a.endTime = :endTime " +
    "and isAvailable = true")
List<Availability> findByStartAndEndTimeAndEsthetician(
    @Param("estheticianId") long estheticianId,
    @Param("startTime") LocalDateTime starTime,
    @Param("endTime") LocalDateTime endTime);

    @Query("SELECT a FROM Availability a WHERE a.esthetician.id = :estheticianId " +
       "AND NOT (a.endTime <= :startTime OR a.startTime >= :endTime)")
List<Availability> findOverlappingEstheticianSlots(
    @Param("estheticianId") Long estheticianId,
    @Param("startTime") LocalDateTime startTime,
    @Param("endTime") LocalDateTime endTime
);
}