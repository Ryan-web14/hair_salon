package com.sni.hairsalon.model;

import jakarta.persistence.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor(staticName = "create")
@Builder
@Entity
@Table(name = "haircut")
public class Haircut {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "haircut_id")
    private long id;

    @Column(name = "haircut_type",nullable = false)
    private String type;

    @Column(name = "duration", nullable = false)
    private int duration;

    @Column(name = "description", nullable = false)
    private String description;
    
    @Column(name = "price", nullable = false)
    private int price;
    
}
