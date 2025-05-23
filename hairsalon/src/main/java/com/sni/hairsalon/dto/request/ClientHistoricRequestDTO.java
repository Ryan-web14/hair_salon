package com.sni.hairsalon.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(staticName = "create")
public class ClientHistoricRequestDTO {
   private String appointmentId;
   private String clientId; 
}
