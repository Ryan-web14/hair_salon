package com.sni.hairsalon.service.serviceImpl;

import org.springframework.stereotype.Service;

import com.sni.hairsalon.repository.ScheduleRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ScheduleServiceImpl {
    
    private final ScheduleRepository scheduleRepo;
}
