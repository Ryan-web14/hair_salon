package com.sni.hairsalon.model;

import jakarta.persistence.*;
import lombok.*;

import com.sni.hairsalon.annotation.IdGeneration;
 
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Table(name = "client")
public class Client extends User {

    @Id
    @IdGeneration
    @Column(name = "client_id")
    private long id;

    @Column(name = "user_id", nullable = false)
    private long user;

    @Column(name = "client_lastname", nullable = false)
    private String lastname;

    @Column(name = "client_firstname", nullable = false)
    private String firstname;

    @Column(name = "client_phone", nullable = false)
    private int phoneNumber;

    //Number of time the client hasn't show themselve to an appointment
    @Column(name = "no_show_count", nullable = false)
    private int noShowCount;

    public Client (String email, String passwordHash, int role){
        super(email, passwordHash,role);
    }

}
