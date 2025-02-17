package com.sni.hairsalon.dto.response;

import lombok.Data;

@Data
public class HaircutResponseDTO {
    private long id;
    //Name of the haircut
    private String type;
    private int duration;
    private String description;
    private int price;
}
