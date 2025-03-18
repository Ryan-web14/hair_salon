package com.sni.hairsalon.dto.request;

import java.time.LocalDateTime;

import com.sni.hairsalon.model.Status;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor(staticName = "create")
public class AppointmentUpdateRequestDTO {
   private String email;
   private String barberId;
   private String estheticianId;
   private String haircutType;
   private String estheticType;
   private LocalDateTime appointmentTime;
   
   @Builder.Default
   private Status status = Status.PENDING;
}