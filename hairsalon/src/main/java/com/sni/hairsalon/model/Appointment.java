package com.sni.hairsalon.model;

import java.time.LocalDateTime;

import com.sni.hairsalon.annotation.IdGeneration;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
//import lombok.RequiredArgsConstructor;;


@Data
@NoArgsConstructor
@AllArgsConstructor(staticName = "create")
@Entity
@Table(name = "appointment")
public class Appointment {

    @Id
    @IdGeneration
    @Column(name = "appointment_id")
    private long id;

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private long client;

    @ManyToOne
    @JoinColumn(name = "barber_id", nullable = false)
    private long barber;

    @ManyToOne
    @JoinColumn(name = "haircut_id", nullable = false)
    private long haircut;

    @Column(name = "appointment_time", nullable = false)
    private LocalDateTime appointmentTime;

    @Column(name = "booked_time", nullable = false)
    private LocalDateTime bookedTime;

    @Column(name = "status")
    private boolean status;

    public Appointment(long client, long barber, long haircut, LocalDateTime bookedTime) {
        this.client = client;
        this.barber = barber;
        this.haircut = haircut;
        this.bookedTime = bookedTime;
        status = false;
        onCreate();
    }

    protected void onCreate(){
        appointmentTime = LocalDateTime.now();
    }

   
}
