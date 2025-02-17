package com.sni.hairsalon.model;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sni.hairsalon.annotation.IdGeneration;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Builder;
import lombok.AllArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name =  "\"user\"")
public class User  {

    @Id
    @IdGeneration
    @Column(name ="user_id")
    private long id;

   @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id")
    private UserRole role;

    @Column(name = "user_email", nullable = false)
    private String email;

    @Column(name = "password", nullable = false)
    @JsonIgnore
    private String passwordHash;

    @Column(name = "last_login")
    @JsonIgnore
    private LocalDateTime last_login;

    @Column(name = "created_at", nullable = false)
    @JsonIgnore
    private LocalDateTime created_at;

    @Column(name = "updated_at")
    @JsonIgnore
    private LocalDateTime updated_at;

    @PrePersist
    public void onCreate(){
        created_at = LocalDateTime.now();
    }
    
}
