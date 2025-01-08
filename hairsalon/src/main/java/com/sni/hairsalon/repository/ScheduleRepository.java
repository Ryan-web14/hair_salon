package com.sni.hairsalon.repository;

import java.sql.Date;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.sni.hairsalon.model.Schedule;

@Repository
public interface ScheduleRepository {
    List<Schedule> findByBarberID(long id);
    List<Schedule> findByDayOfWeek(DayOfWeek dayOfWeek);

    @Query("select s from Schedule s where s.barber_id = :barberId" +
    "and day_of_week = :dayOfWeek" +
    "and s.effectiveFrom <= :date"+
    "and s.effectiveTo>= :date" )
     List<Schedule> findActiveSchedulesByBarberAndDay(
        @Param("barberId") Long barberId,
        @Param("dayOfWeek") DayOfWeek dayOfWeek,
        @Param("date") Date date
    );

    @Query("select s from Schedule s where s.is_recurring = true " +
    "AND s.effectiveFrom <= :date " +
    "AND s.effectiveTo>= :date")
    List<Schedule> findActiveRecurringSchedules(@Param("date") Date date);  

    @Query("select s from Schedule s where s.barber_id = :barberId " +
    "AND s.effectiveTo>= :currentDate")
    List<Schedule> findFutureSchedules(
    @Param("barberId") Long barberId,
    @Param("currentDate") Date currentDate
    );

    @Query("select s from Schedule s WHERE s.barber_id = :barberId " +
           "AND s.day_of_Week = :dayOfWeek " +
           "AND ((s.startTime <= :endTime AND s.end_time >= :startTime) " +
           "OR (s.startTime >= :startTime AND s.startTime <= :endTime)) " +
           "AND s.effectiveFrom <= :date " +
           "AND s.effectiveTo>= :date")
    List<Schedule> findOverlappingSchedules(
        @Param("barberId") Long barberId,
        @Param("dayOfWeek") DayOfWeek dayOfWeek,
        @Param("startTime") LocalDateTime startTime,
        @Param("endTime") LocalDateTime endTime,
        @Param("date") Date date
    );

    Void deleteById(long id);
    Void deleteAll();

}

//Jsql
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