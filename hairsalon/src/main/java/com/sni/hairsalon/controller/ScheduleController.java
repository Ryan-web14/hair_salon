package com.sni.hairsalon.controller;

import java.time.LocalDate;
import java.util.List;

import org.hibernate.sql.Delete;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sni.hairsalon.dto.request.BulkScheduleRequestDTO;
import com.sni.hairsalon.dto.request.ScheduleRequestDTO;
import com.sni.hairsalon.dto.request.ScheduleTemplateRequestDTO;
import com.sni.hairsalon.dto.response.ScheduleResponseDTO;
import com.sni.hairsalon.service.ScheduleService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/v1/schedule")
@RequiredArgsConstructor
public class ScheduleController {
    
    private final ScheduleService scheduleService;

    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<ScheduleResponseDTO> createSchedule(@RequestBody ScheduleRequestDTO request){
        return ResponseEntity.status(HttpStatus.CREATED)
        .body(scheduleService.createSchedule(request));
    }  

    @GetMapping("/{id}/barber")
    public ResponseEntity<List<ScheduleResponseDTO>> getBarberSchedule(@PathVariable Long id){
        
        return ResponseEntity.ok(scheduleService.getBarberSchedule(id));
    }

    @PostMapping("/admin/{id}/barber")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ScheduleResponseDTO> updateSchedule(@PathVariable Long barberId, 
    @RequestBody ScheduleRequestDTO request){

        return ResponseEntity.ok(scheduleService.updateSchedule(barberId, request));
    }

    @GetMapping("/{id}/esthetician")
    public ResponseEntity<List<ScheduleResponseDTO>> getEstheticianSchedule(@PathVariable Long id) {
    return ResponseEntity.ok(scheduleService.getEstheticianSchedule(id));
}

    @DeleteMapping("/{scheduleId}/delete")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<Void> deleteSchedule(@PathVariable Long scheduleId){

        scheduleService.deleteSchedule(scheduleId);
        return ResponseEntity.noContent().build();
    }


    @PostMapping("/template")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<List<ScheduleResponseDTO>> createMultipleSchedules(
        @RequestBody ScheduleTemplateRequestDTO request){

        return ResponseEntity.status(HttpStatus.CREATED)
        .body(scheduleService.createTemplateSchedule(request));
    }

    @GetMapping("/specific-date/{id}/barber")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<ScheduleResponseDTO> getBarberScheduleForDate(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,  @PathVariable Long barberId){

            return ResponseEntity.ok(scheduleService.getBarberScheduleForDate(barberId, date));
        }

    @GetMapping("/current")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<List<ScheduleResponseDTO>> getCurrentSchedule(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date){
            
            return ResponseEntity.ok(scheduleService.getAllCurrentSchedule(date));
        }

    @GetMapping("/current/barber")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity <ScheduleResponseDTO> getTodayScheduleForBarber(@RequestParam  Long barberId){
        
        return ResponseEntity.ok(scheduleService.getBarBerTodayCurrentSchedule(barberId));
    }

    @PostMapping("/bulk")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<List<ScheduleResponseDTO>> createBulkSchedules(
            @RequestBody BulkScheduleRequestDTO request) {
              return ResponseEntity.status(HttpStatus.CREATED)
            .body(scheduleService.bulkCreateSchedules(request));
    }

    @DeleteMapping("/delete")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<Void> deleteAllSchedule(){
        scheduleService.deleteAllSchedule();
        return ResponseEntity.noContent().build();

    }

    @DeleteMapping("/{id}/delete/barber")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<Void> deleteBarberSchedule(@PathVariable long id){

        scheduleService.deleteScheduleByBarberId(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}/delete/esthetician")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<Void> deleteEstheticianSchedule(@PathVariable long id){

        scheduleService.deleteScheduleByEstheticianId(id);
        return ResponseEntity.noContent().build();
    }


}


//TODO work on this function

/*@PostMapping("/create-large")
public ResponseEntity<?> createLargeSchedule(@RequestBody ScheduleRequestDTO request) {
    // Validate request and create schedule without slots
    ScheduleResponseDTO scheduleDto = scheduleService.createScheduleWithoutSlots(request);
    
    // Trigger async process for slot generation
    slotGenerationJobService.scheduleSlotGeneration(request, scheduleDto.getId());
    
    return ResponseEntity.accepted()
        .body(Map.of(
            "message", "Schedule created. Slots will be generated in the background.",
            "scheduleId", scheduleDto.getId(),
            "statusCheckEndpoint", "/v1/schedule/generation-status/" + scheduleDto.getId()
        ));
 */