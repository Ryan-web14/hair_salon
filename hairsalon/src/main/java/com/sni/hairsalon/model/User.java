package com.sni.hairsalon.model;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sni.hairsalon.annotation.IdGeneration;

import jakarta.persistence.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor(staticName = "create")
@RequiredArgsConstructor(staticName = "of")
@Builder
@Entity
@Table(name = "user")
public class User {

    @Id
    @IdGeneration
    @Column(name ="user_id")
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id")
    private int role;

    @Column(name = "user_email", nullable = false)
    @NonNull
    private String email;

    @Column(name = "password", nullable = false)
    @JsonIgnore
    @NonNull
    private String passwordHash;

    @Column(name = "last_login")
    @JsonIgnore
    private LocalDateTime last_login;

    @Column(name = "created_at", nullable = false)
    @JsonIgnore
    private LocalDateTime created_at;

    @Column(name = "updated_at", nullable = false)
    @JsonIgnore
    private LocalDateTime updated_at;

    protected void creationTime(){
        created_at = LocalDateTime.now();
    }

    public User(String email, String passwordHash, int role){
        this.email = email;
        this.passwordHash = passwordHash;
        this.role = role;
        creationTime();
    }

    
}
