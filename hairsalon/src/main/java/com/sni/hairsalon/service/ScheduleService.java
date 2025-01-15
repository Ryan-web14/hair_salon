package com.sni.hairsalon.service;

import java.time.LocalDate;
import java.util.List;

import com.sni.hairsalon.dto.request.ScheduleRequestDTO;
import com.sni.hairsalon.dto.request.ScheduleTemplateRequestDTO;
import com.sni.hairsalon.dto.response.ScheduleResponseDTO;

public interface ScheduleService {

    public ScheduleResponseDTO createSchedule(ScheduleRequestDTO request);
    public List<ScheduleResponseDTO> getBarberSchedule(long barberId);
    public ScheduleResponseDTO updateSchedule(long id, ScheduleRequestDTO request);
    public List<ScheduleResponseDTO> createTemplateSchedule(ScheduleTemplateRequestDTO request);
    //public List<ScheduleResponseDTO> createBulkSchedule(BulkScheduleRequestDTO request);
    //public List<ScheduleResponseDTO> resolveConflicts(Long barberId, LocalDate date);
    //public List<ScheduleResponseDTO> getBarberScheduleForDate(Long barberId, LocalDate date);
    public ScheduleResponseDTO getBarberScheduleForDate(Long barberId, LocalDate date);
    public List<ScheduleResponseDTO> getAllCurrentSchedule(LocalDate date);
    public List<ScheduleResponseDTO> getBarBerTodayCurrentSchedule(long barberId);

}
