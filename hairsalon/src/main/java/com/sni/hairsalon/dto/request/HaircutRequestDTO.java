package com.sni.hairsalon.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(staticName = "create")
public class HaircutRequestDTO {
    //Name of the haircut
    private String type;
    private int duration;
    private String description;
    private int price;

}
