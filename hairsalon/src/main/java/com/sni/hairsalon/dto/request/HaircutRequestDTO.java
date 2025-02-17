package com.sni.hairsalon.dto.request;

import lombok.Data;

@Data
public class HaircutRequestDTO {
    //Name of the haircut
    private String type;
    private int duration;
    private String description;
    private int price;

}
