package com.sni.hairsalon.model;

import java.sql.Date;

import java.time.LocalDateTime;
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
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor(staticName = "create")
@Builder
@Entity
@Table(name = "schedule")
public class Schedule {
    
    @Id
    @IdGeneration
    @Column(name = "schedule_id")
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "barber_id", nullable = false)
    private Barber barber;

    @Column(name = "day_of_week", nullable = false)
    private int dayOfWeek;

    //Start time for the barber
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;

    //End time for the barber
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "end_time",nullable = false)
    private LocalDateTime endTime;

    //To verify if the schdedule is repeating or not
    @Column(name = "is_reccuring")
    private boolean is_recurring;

    //effective date for the schedule
    @Temporal(TemporalType.DATE)
    @Column(name = "effective_from", nullable = false)
    private Date effectiveFrom;
    
    //The schedule is valid to this date
    @Temporal(TemporalType.DATE)
    @Column(name = "effective_to", nullable = false)
    private Date effectiveTo;

    @OneToMany(mappedBy = "schedule", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Availability> availabilities;

    
    
}
