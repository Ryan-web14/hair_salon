package com.sni.hairsalon.model;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sni.hairsalon.annotation.IdGeneration;

import jakarta.persistence.*;
import lombok.*;


@Data
@AllArgsConstructor(staticName = "create")
@RequiredArgsConstructor(staticName = "of")
@Entity
@Builder
@Table(name = "user_session")
public class UserSession {

    @Id
    @IdGeneration
    @Column(name = "session_id")
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private long user; 

    @Column(name = "created_at",nullable = false)
    @JsonIgnore
    private LocalDateTime created_at;

    @Column(name = "expired_at",nullable = false)
    @JsonIgnore
    private LocalDateTime expired_at;

    @Column(name = "token", nullable = false)
    private String token;

    @Column(name = "is_active", nullable = false)
    @JsonIgnore
    private boolean is_active;

    @Column(name = "ip_address", nullable = false)
    private String ipAdress;

    //Information on the browser and device the client is using or the admin
    @Column(name = "user_agent", nullable = false)
    private String userAgent;
    
    
}