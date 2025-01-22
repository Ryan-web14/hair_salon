package com.sni.hairsalon.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.sni.hairsalon.model.Appointment;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment,Long> {
    Optional<Appointment> findAppointmentById(long id);
    List<Appointment> findAppointmentByStatus(int status);

    @Query("select a from Appointment a where a.bookedTime = :bookedTime")
    Appointment findAppointmentByBookedTime(@Param("bookedTime") LocalDateTime bookedTime);

    @Query("select a from Appointment a where a.client.id = :clientId")
    Appointment findAppointmentByClientId(@Param("clientId") long clientId);

    @Query("select a from Appointment a where a.barber.id = :barberId")
    List<Appointment> findAppointmentByBarberId(@Param("barberId") long barberId);

    @Query("Select a from Appointment a where a.status = :status " +
    "and a.appointmentTime between :startTime and :endTime")
    List<Appointment> findByAppointmentTimeBetweenAndStatus(
    @Param("startTime") LocalDateTime startTime,
    @Param("endTime") LocalDateTime endTime,
    @Param("status") int status
);
      @Query("SELECT a FROM Appointment a WHERE a.barber.id = :barberId " +
           "AND DATE(a.appointmentTime) = :date " +
           "AND a.status = :status")
    List<Appointment> findByBarberAndDate(
        @Param("barberId") Long barberId,
        @Param("date") LocalDate date,
        @Param("status") int status
    );
}
