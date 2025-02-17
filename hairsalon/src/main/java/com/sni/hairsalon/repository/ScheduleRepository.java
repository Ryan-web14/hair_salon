package com.sni.hairsalon.repository;

import java.sql.Date;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.sni.hairsalon.model.Schedule;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule,Long > {
    List<Schedule> findByBarberId(long id);
    List<Schedule> findByDayOfWeek(DayOfWeek dayOfWeek);

    @Query("select s from Schedule s where s.barber.id = :barberId " +
    "and s.dayOfWeek = :dayOfWeek " +
    "and s.effectiveFrom <= :date "+
    "and s.effectiveTo>= :date" )
     Optional<List<Schedule>> findActiveSchedulesByBarberAndDay(
        @Param("barberId") Long barberId,
        @Param("dayOfWeek") DayOfWeek dayOfWeek,
        @Param("date") Date date
    );

    @Query("select s from Schedule s where s.is_recurring = true " +
    "AND s.effectiveFrom <= :date " +
    "AND s.effectiveTo>= :date")
    List<Schedule> findActiveRecurringSchedules(@Param("date") Date date);  

    @Query("select s from Schedule s where s.barber.id = :barberId " +
    "AND s.effectiveTo>= :currentDate")
    List<Schedule> findFutureSchedules(
    @Param("barberId") Long barberId,
    @Param("currentDate") Date currentDate
    );

    @Query("SELECT s FROM Schedule s WHERE s.barber.id = :barberId " +
    "AND s.dayOfWeek = :dayOfWeek " +                           
    "AND ((s.startTime <= :endTime AND s.endTime >= :startTime) " +
    "OR (s.startTime >= :startTime AND s.startTime <= :endTime)) " +
    "AND s.effectiveFrom <= :date " +
    "AND s.effectiveTo >= :date")
    List<Schedule> findSchedulesOverlapping(
        @Param("barberId") Long barberId,
        @Param("dayOfWeek") DayOfWeek dayOfWeek,
        @Param("startTime") LocalDateTime startTime,
        @Param("endTime") LocalDateTime endTime,
        @Param("date") Date date
    );

    @Query("SELECT s FROM Schedule s WHERE s.barber.id = :barberId " +
    "AND s.dayOfWeek = :dayOfWeek " +
    "AND ((s.startTime BETWEEN :startTime AND :endTime) " +
    "OR (s.endTime BETWEEN :startTime AND :endTime)) " +
    "AND ((s.effectiveFrom BETWEEN :effectiveFrom AND :effectiveTo) " +
    "OR (s.effectiveTo BETWEEN :effectiveFrom AND :effectiveTo))")
List<Schedule> findOverlappingSchedules(
 @Param("barberId") Long barberId,
 @Param("dayOfWeek") int dayOfWeek,
 @Param("startTime") LocalDateTime startTime,
 @Param("endTime") LocalDateTime endTime,
 @Param("effectiveFrom") Date effectiveFrom,
 @Param("effectiveTo") Date effectiveTo
);

@Query("SELECT s FROM Schedule s WHERE :date BETWEEN s.effectiveFrom AND s.effectiveTo")
List<Schedule> findCurrentSchedules(@Param("date") LocalDate date);    

    @Query("SELECT s FROM Schedule s WHERE s.barber.id = :barberId AND :date BETWEEN s.effectiveFrom AND s.effectiveTo")
List<Schedule> findCurrentSchedulesForBaber(
    @Param("barberId") Long barberId, 
    @Param("date") LocalDate date
);

@Query("SELECT s FROM Schedule s WHERE s.barber.id = :barberId " +
       "AND ((:date BETWEEN s.effectiveFrom AND s.effectiveTo) " +
       "AND (s.dayOfWeek = :dayOfWeek OR s.is_recurring = true))")
List<Schedule> findConflictingSchedules(
    @Param("barberId") Long barberId,
    @Param("date") LocalDate date
);

@Query("SELECT s FROM Schedule s WHERE s.barber.id = :barberId " +
       "AND s.is_recurring = false " +
       "AND :date BETWEEN s.effectiveFrom AND s.effectiveTo")
List<Schedule> findNonRecurringSchedules(
    @Param("barberId") Long barberId,
    @Param("date") LocalDate date
);

@Query("SELECT s FROM Schedule s WHERE s.barber.id = :barberId " +
       "AND s.is_recurring = true " +
       "AND s.dayOfWeek = :dayOfWeek " +
       "AND :date BETWEEN s.effectiveFrom AND s.effectiveTo")
Schedule findRecurringSchedules(
    @Param("barberId") Long barberId,
    @Param("dayOfWeek") int dayOfWeek,
    @Param("date") LocalDate date
);


}


  /*@Repository 
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {    
    List<Schedule> findByBarberId(long id);
    
    List<Schedule> findByDayOfWeek(DayOfWeek dayOfWeek);
    
    @Query("""
        SELECT s FROM Schedule s 
        WHERE s.barber.id = :barberId 
        AND s.dayOfWeek = :dayOfWeek 
        AND s.effectiveFrom <= :date 
        AND s.effectiveTo >= :date
    """)
    List<Schedule> findActiveSchedulesByBarberAndDay(
        @Param("barberId") Long barberId,
        @Param("dayOfWeek") DayOfWeek dayOfWeek,
        @Param("date") Date date
    );

    @Query("""
        SELECT s FROM Schedule s 
        WHERE s.is_recurring = true 
        AND s.effectiveFrom <= :date 
        AND s.effectiveTo >= :date
    """)
    List<Schedule> findActiveRecurringSchedules(@Param("date") Date date);

    @Query("""
        SELECT s FROM Schedule s 
        WHERE s.barber.id = :barberId 
        AND s.effectiveTo >= :currentDate
    """)
    List<Schedule> findFutureSchedules(
        @Param("barberId") Long barberId,
        @Param("currentDate") Date currentDate
    );

    @Query("""
        SELECT s FROM Schedule s 
        WHERE s.barber.id = :barberId 
        AND s.dayOfWeek = :dayOfWeek 
        AND ((s.startTime <= :endTime AND s.endTime >= :startTime) 
        OR (s.startTime >= :startTime AND s.startTime <= :endTime)) 
        AND s.effectiveFrom <= :date 
        AND s.effectiveTo >= :date
    """)
    List<Schedule> findOverlappingSchedules(
        @Param("barberId") Long barberId,
        @Param("dayOfWeek") DayOfWeek dayOfWeek,
        @Param("startTime") LocalDateTime startTime,
        @Param("endTime") LocalDateTime endTime,
        @Param("date") Date date
    );
} */