package com.sni.hairsalon.model;

import jakarta.persistence.*;
import lombok.*;

@Data
@AllArgsConstructor(staticName = "create")
@RequiredArgsConstructor(staticName = "of")
@Entity
@Table(name = "role")
public class UserRole{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private long id;

    @Column(name = "role_name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

}