package com.sni.hairsalon.service;

import java.time.LocalDate;
import java.util.List;

import com.sni.hairsalon.dto.request.ScheduleRequestDTO;
import com.sni.hairsalon.dto.request.ScheduleTemplateRequestDTO;
import com.sni.hairsalon.dto.request.BulkScheduleRequestDTO;
import com.sni.hairsalon.dto.response.ScheduleResponseDTO;

public interface ScheduleService {

    public ScheduleResponseDTO createSchedule(ScheduleRequestDTO request);
    public List<ScheduleResponseDTO> getBarberSchedule(long barberId);
    public ScheduleResponseDTO updateSchedule(long id, ScheduleRequestDTO request);
    public List<ScheduleResponseDTO> createTemplateSchedule(ScheduleTemplateRequestDTO request);
    public List<ScheduleResponseDTO> bulkCreateSchedules(BulkScheduleRequestDTO request);
    public  ScheduleResponseDTO getBarberScheduleForDate(Long barberId, LocalDate date);
    public List<ScheduleResponseDTO> getAllCurrentSchedule(LocalDate date);
    public ScheduleResponseDTO getBarBerTodayCurrentSchedule(long barberId);

}
