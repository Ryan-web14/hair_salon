package com.sni.hairsalon.model;

import java.sql.Date;
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

@Data
@NoArgsConstructor
@AllArgsConstructor(staticName = "create")
@Entity
@Table(name = "schedule")
public class Schedule {
    
    @Id
    @IdGeneration
    @Column(name = "schedule_id")
    private long id;

    @ManyToOne
    @JoinColumn(name = "barber_id", nullable = false)
    private long barber;

    @Column(name = "day_of_week", nullable = false)
    private String dayOfWeek;

    //Start time for the barber
    @Column(name = "start_time", nullable = false)
    private LocalDateTime start_time;

    //End time for the barber
    @Column(name = "end_time",nullable = false)
    private LocalDateTime endTime;

    //To verify if the schdedule is repeating or not
    @Column(name = "is_reccuring")
    private boolean is_recurring;

    //effective date for the schedule
    @Column(name = "effective_from", nullable = false)
    private Date effectiveFrom;
    
    //The schedule is valid to this date
    @Column(name = "effective_to", nullable = false)
    private Date effectiveTo;

    
    
}
