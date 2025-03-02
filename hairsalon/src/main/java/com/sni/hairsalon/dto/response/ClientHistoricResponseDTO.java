package com.sni.hairsalon.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(staticName = "create")
public class ClientHistoricResponseDTO {
    private String id;
    private String appointmentId;
    private String clientId; 
}
