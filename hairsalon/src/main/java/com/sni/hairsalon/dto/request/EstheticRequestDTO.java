package com.sni.hairsalon.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EstheticRequestDTO {
    
    private String type;
    private Integer duration;
    private Integer price;
    private String description;
}