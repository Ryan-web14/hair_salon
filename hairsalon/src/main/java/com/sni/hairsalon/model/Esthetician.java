package com.sni.hairsalon.model;

import java.util.List;

import com.sni.hairsalon.annotation.IdGeneration;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "esthetician  ")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Esthetician {
    
    @Id
    @IdGeneration
    @Column(name = "esthetician_id")
    private long id;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user;
    
    @Column(name = "esthetician_lastname", nullable = false)
    private String lastname;
    
    @Column(name = "esthetician_firstname", nullable = false)
    private String firstname;

    @Column(name = "esthetician_phone", nullable = false)
    private int phoneNumber;

    @Column(name = "esthetician_description")
    private String description;

    @Column(name = "available", nullable = false)
    private boolean available;

    @OneToMany(mappedBy = "esthetician", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Schedule> schedules;

    @OneToMany(mappedBy = "esthetician", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Appointment> appointments;

    
    @OneToMany(mappedBy = "esthetician", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Availability> availabilities;
}

    

    
