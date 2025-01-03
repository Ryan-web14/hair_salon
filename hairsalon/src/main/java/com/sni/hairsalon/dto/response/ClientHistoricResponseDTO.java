package com.sni.hairsalon.dto.response;

import lombok.Data;

@Data
public class ClientHistoricResponseDTO {
    private long id;
    private long appointmentId;
    private long clientId; 
}
