package com.sni.hairsalon.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EstheticResponseDTO {
    
    private Long id;
    private String type;
    private Integer duration;
    private Integer price;
    private String description;
}