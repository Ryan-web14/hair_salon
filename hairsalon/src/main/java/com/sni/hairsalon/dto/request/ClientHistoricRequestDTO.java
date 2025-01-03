package com.sni.hairsalon.dto.request;

import lombok.Data;

@Data
public class ClientHistoricRequestDTO {
   private long appointmentId;
   private long clientId; 
}
