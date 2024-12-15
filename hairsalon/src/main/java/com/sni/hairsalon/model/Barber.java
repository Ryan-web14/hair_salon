package com.sni.hairsalon.model;


import com.sni.hairsalon.annotation.IdGeneration;

import jakarta.persistence.*;
import lombok.*;

@Data
@EqualsAndHashCode(callSuper = false)
@Table(name = "barber")
public class Barber extends User {

    @Id
    @IdGeneration
    @Column(name = "barber_id")
    private long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private long user;

    @Column(name = "barber_lastname", nullable = false)
    private String lastname;

    @Column(name = "barber_firstname", nullable = false)
    private String firstname;

    @Column(name = "barber_phone")
    private int phoneNumber;

    @Column(name = "barber_description")
    private String description;
    
    @Column(name = "available", nullable = false)
    private boolean available;

    public Barber (String email, String passwordHash, int role){
        super(email, passwordHash,role);
    }
}
