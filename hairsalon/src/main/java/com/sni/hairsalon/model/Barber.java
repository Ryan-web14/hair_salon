package com.sni.hairsalon.model;


import com.sni.hairsalon.annotation.IdGeneration;

import jakarta.persistence.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Builder
@Entity
@Table(name = "barber")
public class Barber {

    @Id
    @IdGeneration
    @Column(name = "barber_id")
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

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
    
  /*   @OneToMany(mappedBy = "barber")
    private List<Appointment> appointments; 

    @OneToMany(mappedBy = "barber")
    private List <Schedule> schedule;
    
    public Barber (String email, String passwordHash, UserRole role){
        super(email, passwordHash,role);
    }*/
}
