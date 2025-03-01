package com.sni.hairsalon.model;

import com.sni.hairsalon.annotation.IdGeneration;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor(staticName = "create")
@Entity
@Table(name = "client_historic")
public class ClientHistoric {

    @Id
    @IdGeneration
    @Column(name = "historic_id")
    private long id;

     @ManyToOne(fetch = FetchType.LAZY)
     @JoinColumn(name = "appointment_id")
     private Appointment appointment;

     @ManyToOne(fetch = FetchType.LAZY)
     @JoinColumn(name = "client_id")
     private Client client;

     @Column(name = "count")
     private int count;
    
     
}
