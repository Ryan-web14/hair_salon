package com.sni.hairsalon.model;

import java.time.LocalDateTime;

import com.sni.hairsalon.annotation.IdGeneration;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor(staticName = "create")
@Builder
@Entity
@Table(name = "appointment")
public class Appointment {

    @Id
    @IdGeneration
    @Column(name = "appointment_id")
    private long id;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    @ManyToOne
    @JoinColumn(name = "barber_id", nullable = true)
    private Barber barber;

    @ManyToOne
    @JoinColumn(name = "esthetician_id", nullable = true)
    private Esthetician esthetician;

    @ManyToOne
    @JoinColumn(name = "haircut_id", nullable = true)
    private Haircut haircut;

    @ManyToOne
    @JoinColumn(name = "esthetic_id", nullable = true)
    private Esthetic esthetic;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "appointment_time", nullable = false)
    private LocalDateTime appointmentTime;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "booked_time", nullable = false)
    private LocalDateTime bookedTime;

    @Column(name = "status")
    private int status;

    public Appointment(){
        onCreate();
    }
    
    @PrePersist
    protected void onCreate(){
        this.bookedTime = LocalDateTime.now();
    }
   
  
}


