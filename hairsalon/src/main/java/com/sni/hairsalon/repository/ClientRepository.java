package com.sni.hairsalon.repository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.sni.hairsalon.model.Client;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long>{
   Optional<Client> findById(long id);
   Optional<Client>  findClientByFirstname(String name);
   Optional<Client>  findClientByLastname(String name);
   Optional<Client>  findClientByPhoneNumber(int phone);
   Optional<Client>  deleteById(long id);

    @Query("Select c from Client c where c.user.id = :userId")
    Optional<Client> findClientByUserID(@Param("userId") long userId);

    @Query("Select c from Client c where c.user.email = :email")
    Optional<Client> findByEmail(@Param("email") String email );

    @Query("Select c from Client c where " + 
    "lower(c.lastname) like (concat('%', :lastname, '%')) " +
    "and lower(c.firstname) like (concat('%', :firstname, '%'))")
    Optional<List<Client>> findByFirstAndLastname(
        @Param("lastname") String lastname,
        @Param("firstname") String firstname
    );

     
    @Query(value = "SELECT DISTINCT(c.*) FROM client c, appointment a WHERE " +
    "a.client_id = c.client_id " +
    "AND a.appointment_time >= :startOfMonth "+
    "AND a.appointment_time <= :endOfMonth"
    , nativeQuery= true)
    List<Client> findUniqueClientWithAppointment(@Param("startOfMonth") LocalDate startOfMonth,
    @Param("endOfMonth") LocalDate endOfMonth);

    Page<Client> findAll(Pageable page);

}
