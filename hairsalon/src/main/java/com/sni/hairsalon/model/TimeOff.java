package com.sni.hairsalon.model;

import java.sql.Date;

import java.time.LocalDateTime;

import com.sni.hairsalon.annotation.IdGeneration;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "time_off")
public class TimeOff {

    @Id
    @IdGeneration
    @Column(name = "time_off_id")
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "barber_id", nullable = false)
    private Barber barber;

    @Temporal(TemporalType.DATE)
    @Column(name = "start_date", nullable = false)
    private Date startDate;

    @Temporal(TemporalType.DATE)
    @Column(name = "end_date", nullable = false)
    private Date endDate;

    @Column(name = "status")
    private boolean inVacation;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "request_datetime", nullable = false)
    private LocalDateTime requestDate;

    @Column(name = "reason")
    private String reason;

    private void onCreate(){
        requestDate = LocalDateTime.now();
    }

    public TimeOff(Barber barber, Date startDate, Date endDate, boolean inVacation, LocalDateTime requestDate,
            String reason) {
        this.barber = barber;
        this.startDate = startDate;
        this.endDate = endDate;
        this.inVacation = inVacation;
        this.requestDate = requestDate;
        this.reason = reason;
        onCreate();
    }

    
    

}
