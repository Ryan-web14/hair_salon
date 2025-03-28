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

    
    List<Appointment> findAppointmentByStatusAndClientId(int status, long clientId);

    @Query("select a from Appointment a where a.bookedTime = :bookedTime")
    Appointment findAppointmentByBookedTime(@Param("bookedTime") LocalDateTime bookedTime);

    @Query("select a from Appointment a where a.client.id = :clientId "+
    "and a.status = :status")
    List<Appointment> findAppointmentByClientId(@Param("clientId") long clientId,
    @Param("status") int status);

    @Query("select a from Appointment a where a.barber.id = :barberId ")
    List<Appointment> findAppointmentByBarberId(@Param("barberId") long barberId);

    @Query("select a from Appointment a where " +
    "a.client.id = :clientId " +
    "and a.status = :status")
    List<Appointment> findApppointmentByClientIdAndStatus(@Param("clientId") long clientId,
    @Param("status") int status);
    @Query("Select a from Appointment a where " +
    "lower(a.client.lastname) like concat('%', lower(:lastname), '%') " +
    "and a.status = :status")
    List<Appointment> findAppointmentByClientNameAndStatus(@Param("name") String name,
    @Param("status") int status);
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

    @Query("SELECT a FROM Appointment a WHERE DATE(a.appointmentTime) = :date " +
           "AND a.status = :status")
    List<Appointment> findByDate(
        @Param("date") LocalDate date,
        @Param("status") int status
    );

    @Query("Select COUNT(DISTINCT a.client.id) from Appointment a " +
       "where DATE(a.appointmentTime) = :date " +
       "and a.status = :status")
   int countDistinctClientsForDate(
    @Param("date") LocalDate date, 
    @Param("status") int status
);

@Query("SELECT a FROM Appointment a WHERE a.client.id = :clientId")
List<Appointment> findByClientId(@Param("clientId") Long clientId);

@Query("SELECT a FROM Appointment a WHERE a.client.id = :clientId " +
       "AND DATE(a.appointmentTime) = :date")
List<Appointment> findByClientAndDate(
    @Param("clientId") Long clientId, 
    @Param("date") LocalDate date
);

@Query("SELECT a FROM Appointment a WHERE a.client.id = :clientId " +
       "AND DATE(a.appointmentTime) = :date " +
       "AND a.status = :status " + 
       "ORDER BY a.appointmentTime ASC")
List<Appointment> findByClientAndDateAndStatus(
    @Param("clientId") Long clientId, 
    @Param("date") LocalDate date,
    @Param("status") int status
);

@Query("select a from Appointment a where a.esthetician.id = :estheticianId ")
List<Appointment> findAppointmentByEstheticianId(@Param("estheticianId") long estheticianId);

@Query("SELECT a FROM Appointment a WHERE a.esthetician.id = :estheticianId " +
       "AND DATE(a.appointmentTime) = :date " +
       "AND a.status = :status")
List<Appointment> findByEstheticianAndDate(
    @Param("estheticianId") Long estheticianId,
    @Param("date") LocalDate date,
    @Param("status") int status
);

@Query("SELECT a FROM Appointment a WHERE a.esthetician.id = :estheticianId " +
       "AND a.esthetic.id = :estheticId")
List<Appointment> findByEstheticianAndEstheticService(
    @Param("estheticianId") Long estheticianId,
    @Param("estheticId") Long estheticId
);

@Query("SELECT a FROM Appointment a WHERE a.esthetician IS NOT NULL " +
       "AND DATE(a.appointmentTime) = :date " +
       "AND a.status = :status")
List<Appointment> findByEstheticianDateAndStatus(
    @Param("date") LocalDate date,
    @Param("status") int status
);


@Query("SELECT a FROM Appointment a WHERE a.esthetic.id = :estheticId")
List<Appointment> findByEstheticId(@Param("estheticId") Long estheticId);

@Query("SELECT a FROM Appointment a WHERE a.barber IS NOT NULL")
List<Appointment> findAppointmentsWithBarber();

@Query("SELECT a FROM Appointment a WHERE a.barber IS NOT NULL " +
"AND status = :status")
List<Appointment> findAppointmentsWithBarberAndCompleted(int status);

@Query("SELECT a FROM Appointment a WHERE a.esthetician IS NOT NULL")
List<Appointment> findAppointmentsWithEsthetician();


@Query("SELECT a FROM Appointment a WHERE a.esthetician IS NOT NULL " +
"AND status = :status")
List<Appointment> findAppointmentsWithEstheticianAndCompleted(int status);
@Query("SELECT COUNT(a) FROM Appointment a WHERE a.esthetician.id = :estheticianId " +
       "AND DATE(a.appointmentTime) = :date " +
       "AND a.status = :status")
int countEstheticianAppointmentsForDate(
    @Param("estheticianId") Long estheticianId,
    @Param("date") LocalDate date,
    @Param("status") int status
);

@Query("SELECT a FROM Appointment a WHERE a.client.id = :clientId " +
       "AND a.esthetician.id = :estheticianId " +
       "AND a.status = :status")
List<Appointment> findByClientAndEstheticianAndStatus(
    @Param("clientId") Long clientId,
    @Param("estheticianId") Long estheticianId,
    @Param("status") int status
);

@Query("SELECT a FROM Appointment a WHERE " +
       "a.esthetician.id = :estheticianId " +
       "AND a.appointmentTime BETWEEN :startTime AND :endTime " +
       "AND a.status = :status")
List<Appointment> findByEstheticianAndTimeRangeAndStatus(
    @Param("estheticianId") Long estheticianId,
    @Param("startTime") LocalDateTime startTime,
    @Param("endTime") LocalDateTime endTime,
    @Param("status") int status
);

}


