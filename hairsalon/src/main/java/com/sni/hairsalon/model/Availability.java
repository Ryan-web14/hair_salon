package com.sni.hairsalon.model;

import java.time.LocalDateTime;

import org.springframework.cglib.core.Local;

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

@Data
@NoArgsConstructor
@AllArgsConstructor(staticName = "create")
@Entity
@Table(name = "availability")
public class Availability{

    @Id
    @IdGeneration
    @Column(name = "availability_id")
    private long id;

    @ManyToOne
    @JoinColumn(name = "barber_id", nullable = false)
    private long barber;

    @Column(name = "start_time", nullable = false)
    private LocalDateTime starTime;

    @Column(name = "end_time", nullable = false)
    private LocalDateTime endTime;

    @Column(name = "is_available", nullable = false)
    private boolean is_available;

    @Column(name = "note")
    private String note;
    

}