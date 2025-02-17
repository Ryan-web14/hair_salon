package com.sni.hairsalon.service;

import com.sni.hairsalon.model.Appointment;

public interface SmsService {
    
 public String sendConfirmationSms(Appointment appointment);
 public String sendFirstReminder(Appointment appointment);
 public String sendSecondReminder(Appointment appointment);
 public String sendCancellationSms(Appointment appointment);
 public String sendAppointmentCancellationClient(Appointment appointment);
 public String sendAppointmentCancellationToBarber(Appointment appointment);
public String sendCheckInSms(Appointment appointment);
    }
