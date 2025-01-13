package com.sni.hairsalon.repository;

import java.sql.Date;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import com.sni.hairsalon.model.Barber;

@Repository
public interface BarberRepository extends JpaRepository<Barber, Long>{
    Optional<Barber> findBarberById(long id);
    Barber findBarberByLastname(String lastname);
    List<Barber> findBarberByFirstname(String firstname);
    Barber findBarberByPhoneNumber(int phoneNumber);

    @Override
    @NonNull
    List<Barber> findAll();

    @Query("""
        SELECT DISTINCT b FROM Barber b 
        JOIN Schedule s ON s.barber.id = b.id 
        JOIN Availability a ON a.barber.id = b.id 
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
    List<Barber> findAllAvailableBarbers(
        @Param("dayOfWeek") DayOfWeek dayOfWeek,
        @Param("time") LocalDateTime time,
        @Param("date") Date date
    );


     @Query("""
        SELECT b FROM Barber b 
        JOIN Schedule s ON s.barber.id = b.id 
        JOIN Availability a ON a.barber.id = b.id 
        WHERE b.id = :barberId 
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
    Optional<Barber> findAvailableBarber(
        @Param("barberId") Long barberId,
        @Param("dayOfWeek") DayOfWeek dayOfWeek,
        @Param("time") LocalDateTime time,
        @Param("date") Date date
    );

    @Query("""
        SELECT b FROM Barber b 
        JOIN Schedule s ON s.barber.id = b.id 
        JOIN Availability a ON a.barber.id = b.id 
        WHERE b.id = :barberId 
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
    Optional<Barber> findBarberAvailableInTimeRange(
        @Param("barberId") Long barberId,
        @Param("dayOfWeek") DayOfWeek dayOfWeek,
        @Param("startTime") LocalDateTime startTime,
        @Param("endTime") LocalDateTime endTime,
        @Param("date") Date date
    );

    //not correct queries
    /*@Query("SELECT DISTINCT b FROM Barber b " +
           "JOIN b.availabilities a " +
           "WHERE a.startTime <= :time " +
           "AND a.endTime >= :time " +
           "AND a.isAvailable = true")
    List<Barber> findAvailableBarbers(
        @Param("time") LocalTime time
    );


    @Query("SELECT DISTINCT b FROM Barber b " +
    "JOIN b.schedules s " +
    "LEFT JOIN b.availabilities a " +
    "WHERE s.dayOfWeek = :dayOfWeek " +
    "AND s.startTime <= :time " +
    "AND s.endTime >= :time " +
    "AND s.effectiveFrom <= :date " +
    "AND (s.effectiveTo IS NULL OR s.effectiveTo >= :date) " +
    "AND s.isReccuring = true " +
    "AND (a IS NULL OR (a.isAvailable = true AND a.startTime <= :time AND a.endTime >= :time))")
List<Barber> findAvailableBarbersConsideringBoth(
 @Param("dayOfWeek") DayOfWeek dayOfWeek,
 @Param("time") LocalTime time,
 @Param("date") LocalDate date
);*/

}
