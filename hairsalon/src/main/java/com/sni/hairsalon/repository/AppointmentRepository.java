package com.sni.hairsalon.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.sni.hairsalon.model.Appointment;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment,Long> {
    Appointment findAppointmentById(long id);
    List<Appointment> findAppointmentByStatus(int status);

    @Query("select a from Appointment a where a.bookedTime = :bookedTime")
    Appointment findAppointmentByBookedTime(@Param("bookedTime") LocalDateTime bookedTime);

    @Query("select a from Appointment a where a.client.id = :clientId")
    Appointment findAppointmentByClientId(@Param("clientId") long clientId);

    @Query("select a from Appointment a where a.barber.id = :barberId")
    List<Appointment> findAppointmentByBarberId(@Param("barberId") long barberId);
}
