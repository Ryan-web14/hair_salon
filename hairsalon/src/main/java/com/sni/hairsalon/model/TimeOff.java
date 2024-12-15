package com.sni.hairsalon.model;

import java.sql.Date;
import java.sql.Time;
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
@AllArgsConstructor
@Entity
@Table(name = "time_off")
public class TimeOff {

    @Id
    @IdGeneration
    @Column(name = "time_off_id")
    private long id;

    @ManyToOne
    @JoinColumn(name = "barber_id", nullable = false)
    private long barber;

    @Column(name = "start_date", nullable = false)
    private Date startDate;

    @Column(name = "end_date", nullable = false)
    private Date endDate;

    @Column(name = "status")
    private boolean inVacation;

    @Column(name = "request_datetime", nullable = false)
    private LocalDateTime requestDate;

    @Column(name = "reason")
    private String reason;

    private void onCreate(){
        requestDate = LocalDateTime.now();
    }

    public TimeOff(long barber, Date startDate, Date endDate, boolean inVacation, LocalDateTime requestDate,
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
