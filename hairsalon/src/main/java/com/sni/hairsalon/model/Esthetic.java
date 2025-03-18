package com.sni.hairsalon.model;

import jakarta.persistence.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor(staticName = "create")
@Builder
@Entity
@Table(name = "esthetic")
public class Esthetic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "esthetic_id")
    private long id;

    @Column(name = "service_type", nullable = false)
    private String type;

    @Column(name = "duration", nullable = false)
    private int duration;

    @Column(name = "description", nullable = false)
    private String description;
    
    @Column(name = "price", nullable = false)
    private int price;
    
}