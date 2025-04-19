package com.sni.hairsalon.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.Builder;

import java.util.Set;

import com.sni.hairsalon.annotation.IdGeneration;
 
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "client")
public class Client{

    @Id
    @IdGeneration
    @Column(name = "client_id")
    private long id;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "client_lastname", nullable = false)
    private String lastname;

    @Column(name = "client_firstname", nullable = false)
    private String firstname;

    @Column(name = "client_phone", nullable = false)
    private int phoneNumber;

    //Number of time the client hasn't show themselve to an appointment
    @Column(name = "no_show_count", nullable = false)
    @Builder.Default private int noShowCount = 0;

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Appointment> appointments;
}
