package com.sni.hairsalon.service.serviceImpl;
/*see how to put the appointment in progress too */
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import com.sni.hairsalon.dto.request.AppointmentRequestDTO;
import com.sni.hairsalon.dto.request.AppointmentUpdateRequestDTO;
import com.sni.hairsalon.dto.response.AppointmentResponseDTO;
import com.sni.hairsalon.dto.response.ScheduleResponseDTO;
import com.sni.hairsalon.exception.ResourceNotFoundException;
import com.sni.hairsalon.mapper.AppointmentMapper;
import com.sni.hairsalon.model.Appointment;
import com.sni.hairsalon.model.Barber;
import com.sni.hairsalon.model.Client;
import com.sni.hairsalon.model.Esthetic;
import com.sni.hairsalon.model.Esthetician;
import com.sni.hairsalon.model.Haircut;
import com.sni.hairsalon.repository.AppointmentRepository;
import com.sni.hairsalon.repository.BarberRepository;
import com.sni.hairsalon.repository.ClientRepository;
import com.sni.hairsalon.repository.EstheticRepository;
import com.sni.hairsalon.repository.EstheticianRepository;
import com.sni.hairsalon.repository.HaircutRepository;
import com.sni.hairsalon.repository.UserRepository;
import com.sni.hairsalon.service.AppointmentService;
import com.sni.hairsalon.service.AvailabilityService;
import com.sni.hairsalon.service.EmailService;
import com.sni.hairsalon.service.ScheduleService;
import com.sni.hairsalon.service.SmsService;
import com.sni.hairsalon.model.Status;
import com.sni.hairsalon.model.User;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class AppointmentServiceImpl implements AppointmentService {

  private final ScheduleService scheduleService;
  private final AvailabilityService availabilityService;
  private final EmailService mailService;
  private final BarberRepository barberRepo;
  private final HaircutRepository haircutRepo;
  private final AppointmentRepository appointmentRepo;
  private final AppointmentMapper mapper;
  private final SmsService smsService;
  private final UserRepository userRepo;
  private final EstheticianRepository estheticianRepo;
  private final ClientRepository clientRepo;
  private final EstheticRepository estheticRepo;

  
 @Transactional
@Override
public AppointmentResponseDTO createAppointment(AppointmentRequestDTO request) {
    User user = userRepo.findUserByEmail(request.getEmail())
        .orElseThrow(() -> new ResourceNotFoundException("User not Found"));
    
    Client client = clientRepo.findClientByUserID(user.getId())
        .orElseThrow(() -> new ResourceNotFoundException("Client not found"));
    
    LocalDate appointmentDate = request.getAppointmentTime().toLocalDate();
    LocalDateTime appointmentDateTime = request.getAppointmentTime();
    LocalTime appointmentTime = request.getAppointmentTime().toLocalTime();
    
    Appointment appointment = Appointment.builder()
        .client(client)
        .appointmentTime(appointmentDateTime)
        .status(Status.CONFIRMED.getCode())
        .build();

    if (request.getHaircutType() != null && !request.getHaircutType().isEmpty()) {
        Haircut haircut = findHaircutByTypeFlexible(request.getHaircutType());
        appointment.setHaircut(haircut);
    } else if (request.getEstheticType() != null && !request.getEstheticType().isEmpty()) {
        Esthetic estheticService = findEstheticByTypeFlexible(request.getEstheticType());
        appointment.setEsthetic(estheticService);
    } else {
        throw new IllegalArgumentException("Un type de service (coiffure ou soin esthétique) doit être spécifié");
    }
    
    ScheduleResponseDTO schedule;
    int serviceDuration;
    
    if (request.getBarberId() != null && !request.getBarberId().isEmpty()) {
        Barber barber = barberRepo.findById(Long.parseLong(request.getBarberId()))
            .orElseThrow(() -> new ResourceNotFoundException("Barber not found"));
        
        appointment.setBarber(barber);
        schedule = scheduleService.getBarberScheduleForDate(Long.parseLong(request.getBarberId()), appointmentDate);
        serviceDuration = appointment.getHaircut().getDuration();
        
        availabilityService.makeProviderSlotUnavailable("barber", barber.getId(), appointmentDateTime, serviceDuration);
        
    } else if (request.getEstheticianId() != null && !request.getEstheticianId().isEmpty()) {
        Esthetician esthetician = estheticianRepo.findById(Long.parseLong(request.getEstheticianId()))
            .orElseThrow(() -> new ResourceNotFoundException("Esthetician not found"));
        
        appointment.setEsthetician(esthetician);
        schedule = scheduleService.getEstheticianScheduleForDate(Long.parseLong(request.getEstheticianId()), appointmentDate);
        serviceDuration = appointment.getEsthetic().getDuration();
        
        // check for availability and make them unavailable
        availabilityService.makeProviderSlotUnavailable("esthetician", esthetician.getId(), appointmentDateTime, serviceDuration);
        
    } else {
        throw new IllegalArgumentException("Un prestataire (barbier ou esthéticienne) doit être spécifié");
    }
    
    // checking of hours
    LocalTime startSchedule = schedule.getStartTime().toLocalTime();
    LocalTime endSchedule = schedule.getEndTime().toLocalTime();
    
    if (appointmentTime.isBefore(startSchedule) || appointmentTime.isAfter(endSchedule)) {
        throw new IllegalStateException("L'heure du rendez-vous ne correspond pas aux heures du prestataire : " +
            "début : " + schedule.getStartTime() + " fin : " + schedule.getEndTime());
    }
    
    Appointment verifiedAppointment = checkAppointmentTodayDate(appointment);
    appointmentRepo.save(verifiedAppointment);
    
    AppointmentResponseDTO response = mapper.toDto(appointment);
    
    // Envoi des notifications
    mailService.sendAppointmentConfirmation(client.getUser().getEmail(), response);
    
    if (appointment.getBarber() != null) {
        mailService.sendBarberNotificationOfNewAppointment(response.getBarberEmail(), verifiedAppointment);
    } else if (appointment.getEsthetician() != null) {
        mailService.sendEstheticianNotificationOfNewAppointment(response.getEstheticianEmail(), verifiedAppointment);
    }
    
    // smsService.sendConfirmationSms(appointment);
    return response;
}

private Haircut findHaircutByTypeFlexible(String requestType) {
  if (requestType == null || requestType.trim().isEmpty()) {
      throw new ResourceNotFoundException("Haircut type cannot be empty");
  }
  
  String normalizedRequestType = requestType.toLowerCase().replaceAll("\\s+", "");
  
  return haircutRepo.findAll().stream()
      .filter(h -> {
          String normalizedType = h.getType().toLowerCase().replaceAll("\\s+", "");
          return normalizedType.equals(normalizedRequestType);
      })
      .findFirst()
      .orElseThrow(() -> new ResourceNotFoundException("Haircut not found: " + requestType));
}

private Esthetic findEstheticByTypeFlexible(String requestType) {
  if (requestType == null || requestType.trim().isEmpty()) {
      throw new ResourceNotFoundException("Esthetic type cannot be empty");
  }
  
  String normalizedRequestType = requestType.toLowerCase().replaceAll("\\s+", "");
  
  return estheticRepo.findAll().stream()
      .filter(e -> {
          String normalizedType = e.getType().toLowerCase().replaceAll("\\s+", "");
          return normalizedType.equals(normalizedRequestType);
      })
      .findFirst()
      .orElseThrow(() -> new ResourceNotFoundException("Esthetic service not found: " + requestType));
}

@Override
public AppointmentResponseDTO updateAppointmentByAdmin(long id, AppointmentUpdateRequestDTO request) {
   Appointment appointment = appointmentRepo.findById(id)
       .orElseThrow(() -> new ResourceNotFoundException("Appointment not found"));
   
   User user = userRepo.findUserByEmail(request.getEmail())
       .orElseThrow(() -> new ResourceNotFoundException("User not Found"));
   
   Client client = clientRepo.findClientByUserID(user.getId())
       .orElseThrow(() -> new ResourceNotFoundException("Client not found"));
   
   LocalDate appointmentDate = request.getAppointmentTime().toLocalDate();
   LocalDateTime appointmentDateTime = request.getAppointmentTime();
   LocalTime appointmentTime = request.getAppointmentTime().toLocalTime();
   
   ScheduleResponseDTO schedule;
   
   if (request.getBarberId() != null && !request.getBarberId().isEmpty()) {
       Barber barber = barberRepo.findById(Long.parseLong(request.getBarberId()))
           .orElseThrow(() -> new ResourceNotFoundException("Barber not found"));
       
       if (!barber.isAvailable()) {
           throw new IllegalAccessError("Barber unavailable");
       }
       
       Haircut haircut = findHaircutByTypeFlexible(request.getHaircutType());
       schedule = scheduleService.getBarberScheduleForDate(barber.getId(), appointmentDate);
       
       if (appointmentTime.isBefore(schedule.getStartTime().toLocalTime()) || 
           appointmentTime.isAfter(schedule.getEndTime().toLocalTime())) {
           throw new IllegalStateException("The appointment time doesn't match the barber hour" + 
               "startTime: " + schedule.getStartTime() + " endTime: " + schedule.getEndTime());
       }
       
       availabilityService.makeProviderSlotUnavailable("barber", barber.getId(), appointmentDateTime, haircut.getDuration());
       
       appointment.setBarber(barber);
       appointment.setEsthetician(null);
       appointment.setHaircut(haircut);
       appointment.setEsthetic(null);
       
       appointmentRepo.save(appointment);
       mailService.sendRescheduleAppointmentToBarber(barber.getUser().getEmail(), appointment);
       
   } else if (request.getEstheticianId() != null && !request.getEstheticianId().isEmpty()) {
       Esthetician esthetician = estheticianRepo.findById(Long.parseLong(request.getEstheticianId()))
           .orElseThrow(() -> new ResourceNotFoundException("Esthetician not found"));
       
       if (!esthetician.isAvailable()) {
           throw new IllegalAccessError("Esthetician unavailable");
       }
       
       Esthetic esthetic = findEstheticByTypeFlexible(request.getEstheticType());
       schedule = scheduleService.getEstheticianScheduleForDate(esthetician.getId(), appointmentDate);
       
       if (appointmentTime.isBefore(schedule.getStartTime().toLocalTime()) || 
           appointmentTime.isAfter(schedule.getEndTime().toLocalTime())) {
           throw new IllegalStateException("The appointment time doesn't match the esthetician hour" + 
               "startTime: " + schedule.getStartTime() + " endTime: " + schedule.getEndTime());
       }
       
       availabilityService.makeProviderSlotUnavailable("esthetician", esthetician.getId(), appointmentDateTime, esthetic.getDuration());
       
       appointment.setEsthetician(esthetician);
       appointment.setBarber(null);
       appointment.setEsthetic(esthetic);
       appointment.setHaircut(null);
       
       appointmentRepo.save(appointment);
       mailService.sendRescheduleAppointmentToEsthetician(esthetician.getUser().getEmail(), appointment);
   } else {
       throw new IllegalArgumentException("Either barberId or estheticianId must be provided");
   }
   
   appointment.setClient(client);
   appointment.setAppointmentTime(appointmentDateTime);
   appointment.setStatus(Status.CONFIRMED.getCode());
   
   appointmentRepo.save(appointment);
   
   AppointmentResponseDTO response = mapper.toDto(appointment);
   mailService.sendRescheduleAppointmentToClient(client.getUser().getEmail(), response);
   
   return response;
}


  @Override
  public AppointmentResponseDTO updateSAppointmentStatus(long id, int status) {

    Appointment appointment = appointmentRepo.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Appointment not found"));
    appointment.setStatus(status);
    appointmentRepo.save(appointment);

    return mapper.toDto(appointment);
  }

  @Override
  @Transactional
  public List<AppointmentResponseDTO> getAllAppointment() {

    List<Appointment> appointments = appointmentRepo.findAll();

    return appointments.stream()
        .map(appointment -> mapper.toDto(appointment))
        .collect(Collectors.toList());

  }

  @Override
@Transactional
public List<AppointmentResponseDTO> getAllBarberAppointments() {
    List<Appointment> appointments = appointmentRepo.findAppointmentsWithBarber();
    return appointments.stream()
        .map(appointment -> mapper.toDto(appointment))
        .collect(Collectors.toList());
}

@Override
@Transactional
public List<AppointmentResponseDTO> getAllBarberCompletedAppointments() {
    List<Appointment> appointments = appointmentRepo.findAppointmentsWithBarberAndCompleted(Status.COMPLETED.getCode());
    return appointments.stream()
        .map(appointment -> mapper.toDto(appointment))
        .collect(Collectors.toList());
}

@Override
@Transactional
public List<AppointmentResponseDTO> getAllEstheticianAppointments() {
    List<Appointment> appointments = appointmentRepo.findAppointmentsWithEsthetician();
    return appointments.stream()
        .map(appointment -> mapper.toDto(appointment))
        .collect(Collectors.toList());
}

@Override
@Transactional
public List<AppointmentResponseDTO> getAllEstheticianCompletedAppointments() {
    List<Appointment> appointments = appointmentRepo.findAppointmentsWithEstheticianAndCompleted(Status.COMPLETED.getCode());
    return appointments.stream()
        .map(appointment -> mapper.toDto(appointment))
        .collect(Collectors.toList());
}

  @Override
  public List<AppointmentResponseDTO> getClientCompletedAppointment(String email) {

    Client client = clientRepo.findByEmail(email)
        .orElseThrow(() -> new ResourceNotFoundException("Client not found"));
    List<Appointment> appointments = appointmentRepo.findApppointmentByClientIdAndStatus(client.getId(),
        Status.COMPLETED.getCode());

    return appointments.stream()
        .map(appointment -> mapper.toDto(appointment))
        .collect(Collectors.toList());
  }

  @Override
  public List<AppointmentResponseDTO> getAllBarberAppointment(LocalDate date) {

    List<Appointment> appointments = appointmentRepo.findByDate(date, Status.CONFIRMED.getCode());

    return appointments.stream()
        .map(appointment -> mapper.toDto(appointment))
        .collect(Collectors.toList());
  }

  @Override
public List<AppointmentResponseDTO> getAllEstheticianAppointment(LocalDate date) {
    // Find all confirmed esthetician appointments for the given date
    List<Appointment> appointments = appointmentRepo.findByEstheticianDateAndStatus(
        date, Status.CONFIRMED.getCode());

    return appointments.stream()
        .map(appointment -> mapper.toDto(appointment))
        .collect(Collectors.toList());
}

  @Override
  public List<AppointmentResponseDTO> getBarberAppointment(long barberId) {

    LocalDateTime now = LocalDateTime.now();
    List<Appointment> appointments = appointmentRepo.findByAppointmentTimeBetweenAndStatus(
        now.with(LocalTime.MIDNIGHT).truncatedTo(ChronoUnit.MINUTES),
        now.with(LocalTime.MAX).truncatedTo(ChronoUnit.MINUTES),
        Status.CONFIRMED.getCode());

    return appointments.stream()
        .map(appointment -> mapper.toDto(appointment))
        .collect(Collectors.toList());
  }

@Override
public List<AppointmentResponseDTO> getEstheticianAppointment(long estheticianId) {
    LocalDateTime now = LocalDateTime.now();
    List<Appointment> appointments = appointmentRepo.findByEstheticianAndTimeRangeAndStatus(
        estheticianId,
        now.with(LocalTime.MIDNIGHT).truncatedTo(ChronoUnit.MINUTES),
        now.with(LocalTime.MAX).truncatedTo(ChronoUnit.MINUTES),
        Status.CONFIRMED.getCode());

    return appointments.stream()
        .map(appointment -> mapper.toDto(appointment))
        .collect(Collectors.toList());
}

  @Override
  public List<AppointmentResponseDTO> getClientAppointment(long clientId) {

    Client client = clientRepo.findById(clientId)
        .orElseThrow(() -> new ResourceNotFoundException("Client not found"));

    List<Appointment> appointments = appointmentRepo.findAppointmentByClientId(client.getId(),
        Status.CONFIRMED.getCode());

    return appointments.stream()
        .map(appointment -> mapper.toDto(appointment))
        .collect(Collectors.toList());

  }

  @Override
  public List<AppointmentResponseDTO> searchAppointmentByClientName(String name, int status) {

    List<Appointment> appointments = appointmentRepo.findAppointmentByClientNameAndStatus(name, status);

    return appointments.stream()
        .map(appointment -> mapper.toDto(appointment))
        .collect(Collectors.toList());
  }

  @Override
  public List<AppointmentResponseDTO> getCompletedAppointment() {

    List<Appointment> appointments = appointmentRepo.findByDate(LocalDate.now(), Status.COMPLETED.getCode());

    return appointments.stream()
        .map(appointment -> mapper.toDto(appointment))
        .collect(Collectors.toList());
  }

  @Override
  public void cancelAppointmentByClient(long id, String clientEmail){

    Appointment cancelAppointment = appointmentRepo.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Appointment not found"));
    Client client = clientRepo.findByEmail(clientEmail)
        .orElseThrow(() -> new ResourceNotFoundException("Client not found"));

    if (cancelAppointment.getClient().getId() != client.getId()) {

      throw new AccessDeniedException("You can only cancel your appointment");
    }

    if (cancelAppointment.getAppointmentTime().isBefore(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES))) {

      throw new IllegalStateException("Cannot cancel past appointment");
    }

    if (cancelAppointment.getAppointmentTime().plusMinutes(15) == LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES)) {

      throw new IllegalStateException("Cannot cancel appoint 15 min before");
    }
    cancelAppointment.setStatus(Status.CANCELLED_BY_CLIENT.getCode());
    appointmentRepo.save(cancelAppointment);
    mailService.sendAppointmentCancellationToClient(client.getUser().getEmail(),
        mapper.toDto(cancelAppointment));
    mailService.sendAppointmentCancellationToBarber(cancelAppointment.getBarber().getUser().getEmail(),
        mapper.toDto(cancelAppointment));

    return;
  }

  @Override
  public AppointmentResponseDTO checkIn(String email) {
      Client client = clientRepo.findByEmail(email)
          .orElseThrow(() -> new ResourceNotFoundException("Client not found"));
          
      LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES).plusHours(1);
      LocalDate currentDate = now.toLocalDate();
      
      // Find all confirmed appointments for the client on the current day
      List<Appointment> appointments = appointmentRepo.findByClientAndDateAndStatus(client.getId(),
          currentDate, Status.CONFIRMED.getCode());
  
      if (appointments.isEmpty()) {
          throw new ResourceNotFoundException("No confirmed appointment found for the day");
      }
  
      // Get the closest appointment by time
      Appointment appointment = appointments.stream()
          .min(Comparator.comparing(Appointment::getAppointmentTime))
          .orElseThrow(() -> new IllegalStateException("No appointment retrieve"));
      
      LocalDateTime appointmentTime = appointment.getAppointmentTime();
      
      // Verify if check-in is done within the allowed time window
      validateCheckInTime(now, appointmentTime);
  
      // Update status and save the appointment
      appointment.setStatus(Status.CHECK_IN.getCode());
      appointmentRepo.save(appointment);
      
      // Notify the appropriate service provider
      if (appointment.getBarber() != null) {
          notifyBarber(appointment);
      } else if (appointment.getEsthetician() != null) {
          notifyEsthetician(appointment);
      }
  
      return mapper.toDto(appointment);
  }

  private void validateCheckInTime(LocalDateTime now, LocalDateTime appointmentTime){
    if (now.isBefore(appointmentTime.minusMinutes(16))) {
      throw new IllegalStateException("Too early to check in");
  }
  
  if (now.isAfter(appointmentTime.plusMinutes(15))) {
      throw new IllegalStateException("Too late to check in");
  }
  }
  @Override
  @Transactional
  public void cancelAppointment(long id) {
      // Find the appointment by ID or throw exception if not found
      Appointment cancelAppointment = appointmentRepo.findById(id)
          .orElseThrow(() -> new ResourceNotFoundException("Appointment not found"));
      
      // Update the appointment status to cancelled
      cancelAppointment.setStatus(Status.CANCELLED_BY_PROVIDER.getCode());
      
      // Get the client's email
      String clientEmail = cancelAppointment.getClient().getUser().getEmail();
      
      // Check if this is a barber appointment or an esthetician appointment
      if (cancelAppointment.getBarber() != null) {
          // For barber appointments, send notification to the barber
          String barberEmail = cancelAppointment.getBarber().getUser().getEmail();
          mailService.sendAppointmentCancellationToBarber(barberEmail, mapper.toDto(cancelAppointment));
      } else if (cancelAppointment.getEsthetician() != null) {
          // For esthetician appointments, send notification to the esthetician
          String estheticianEmail = cancelAppointment.getEsthetician().getUser().getEmail();
          mailService.sendAppointmentCancellationToEsthetician(estheticianEmail, mapper.toDto(cancelAppointment));
      }
      
      // Always send cancellation notification to the client
      mailService.sendAppointmentCancellationToClient(clientEmail, mapper.toDto(cancelAppointment));
      
      // Save the updated appointment
      appointmentRepo.save(cancelAppointment);
  }

  @Override
  public void makeAppointmentCompleted(long id) {

    Appointment appointment = appointmentRepo.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Appointment not found"));

    if (appointment.getStatus() == 6 || appointment.getStatus() == 7 || appointment.getStatus() == 8) {
      throw new IllegalStateException("Appointment can't be completed");
    }

    appointment.setStatus(6);
    appointmentRepo.save(appointment);
    return;
  }

  @Override
  public void deleteAllAppointment(){
    appointmentRepo.deleteAll();
    return;
  }
  @Override
  @Scheduled(fixedRate = 30000)
  public int countAppointmentForTheDay() {
      List<AppointmentResponseDTO> barberAppointments = getAllBarberAppointment(LocalDate.now());
      List<AppointmentResponseDTO> estheticianAppointments = getAllEstheticianAppointment(LocalDate.now());
      
      return barberAppointments.size() + estheticianAppointments.size();
  }

  @Override
  @Scheduled(fixedRate = 30000)
  public int clientCount() {

    LocalDate now = LocalDate.now();

    int count = appointmentRepo.countDistinctClientsForDate(now, 3);

    return count;
  }

  @Scheduled(fixedRate = 100000)
  public void monitorCheckInAppointment(){

      LocalDateTime now = LocalDateTime.now().plusHours(1).truncatedTo(ChronoUnit.MINUTES);
      List<Appointment> appointments = appointmentRepo.findAppointmentByStatus(4);

      for(Appointment apt  : appointments){

        if (isWithinOneMinute(apt.getAppointmentTime(), now)) {
          // Update the appointment status to 5
          apt.setStatus(5);
          appointmentRepo.save(apt);

        }
      }
  }
  
  @Scheduled(fixedRate = 100000)
  public void InprogressToCompleted(){

    List<Appointment> appointments = appointmentRepo.findAppointmentByStatus(5);
    LocalDateTime now = LocalDateTime.now();

    for(Appointment apt : appointments){

      makeInProgressToCompleted(apt, now);
    }

  }

  @Scheduled(fixedRate = 300000)
  public void monitorAppointmentTime() {
    LocalDateTime now = LocalDateTime.now().plusHours(1);
    List<Appointment> appointments = appointmentRepo.findByAppointmentTimeBetweenAndStatus(
        now.with(LocalTime.MIDNIGHT).truncatedTo(ChronoUnit.MINUTES),
        now.with(LocalTime.MAX).truncatedTo(ChronoUnit.MINUTES),
        Status.CONFIRMED.getCode());

    for (Appointment appointment : appointments) {
      checkAppointmentTime(appointment, now);
    }
  }

  @Scheduled(cron = "0 0 7 * * *", zone = "Africa/Lagos")
  public void sendDailyAppointmentScheduleToBarber() {
    LocalDate today = LocalDate.now();
    List<Barber> barbers = barberRepo.findAll();

    for (Barber barber : barbers) {
      List<Appointment> appointments = appointmentRepo.findByBarberAndDate(barber.getId(),
          today, Status.CONFIRMED.getCode());

      if (!appointments.isEmpty()) {
        mailService.sendDailyScheduleToBarber(barber.getUser().getEmail(), appointments);
      }
    }

  }

  @Scheduled(cron = "0 0 7 * * *", zone = "Africa/Lagos")
public void sendDailyAppointmentScheduleToEsthetician() {
    LocalDate today = LocalDate.now();
    List<Esthetician> estheticians = estheticianRepo.findAll();

    for (Esthetician esthetician : estheticians) {
        List<Appointment> appointments = appointmentRepo.findByEstheticianAndDate(esthetician.getId(),
                today, Status.CONFIRMED.getCode());

        if (!appointments.isEmpty()) {
            mailService.sendDailyScheduleToEsthetician(esthetician.getUser().getEmail(), appointments);
        }
    }
}

  private void notifyBarber(Appointment apt) {
    mailService.sendCheckIn(apt);
    return;
  }

  private void notifyEsthetician(Appointment apt) {
    mailService.sendCheckInToEsthetician(apt);
    return;
}

public List<AppointmentResponseDTO> getMyEstheticianAppointment(String email) {
    Esthetician esthetician = estheticianRepo.findByEmail(email)
        .orElseThrow(() -> new ResourceNotFoundException("Esthetician not found"));
    
    return getEstheticianAppointment(esthetician.getId());
}
  
  public List<AppointmentResponseDTO> getMyBarberAppointment(String email){

    Barber barber = barberRepo.findByEmail(email)
    .orElseThrow(()-> new ResourceNotFoundException("Client not found"));

    return getBarberAppointment(barber.getId());

  }

  private void makeInProgressToCompleted(Appointment appointment, LocalDateTime now) {
    LocalDateTime endTime;
    
    if (appointment.getBarber() != null) {
        endTime = appointment.getAppointmentTime().plusMinutes(appointment.getHaircut().getDuration());
    } else if (appointment.getEsthetician() != null) {
        endTime = appointment.getAppointmentTime().plusMinutes(appointment.getEsthetic().getDuration());
    } else {
        return;
    }
    
    if (endTime.isBefore(now.plusMinutes(10))) {
        appointment.setStatus(6);
        appointmentRepo.save(appointment);
    }
}

  private void checkAppointmentTime(Appointment appointment, LocalDateTime now) {
    LocalDateTime appointmentTime = appointment.getAppointmentTime();
    Duration difference = Duration.between(appointmentTime, now);
    long late = difference.toMinutes();

    if (late >= 5 && late < 10) {
      mailService.sendFirstReminder(appointment.getClient()
          .getUser().getEmail(), mapper.toDto(appointment));
    } else if (late >= 10 && late < 15) {
      mailService.sendSecondReminder(appointment.getClient()
          .getUser().getEmail(), mapper.toDto(appointment));
    } else if (late >= 15) {
      appointment.setStatus(Status.NO_SHOW.getCode());
      appointmentRepo.save(appointment);
      mailService.sendCancellationNotification(appointment.getClient()
          .getUser().getEmail(), mapper.toDto(appointment));
      mailService.sendAppointmentCancellationToBarber(appointment.getBarber()
          .getUser().getEmail(), mapper.toDto(appointment));
    }
  }

  private boolean isWithinOneMinute(LocalDateTime time1, LocalDateTime time2) {
    long diffInSeconds = Math.abs(time1.until(time2, ChronoUnit.SECONDS));
    return diffInSeconds <= 60; // 1 minute = 60 seconds
}

private Appointment checkAppointmentTodayDate(Appointment appointment) {
    LocalDateTime now = LocalDateTime.now().plusHours(1);
  
    if(appointment.getAppointmentTime().toLocalDate().equals(now.toLocalDate())) {
        //appointment must be taken at least 30 minute before the appointment time  
        if(now.plusMinutes(30).isAfter(appointment.getAppointmentTime())) {
            throw new IllegalStateException("Le rendez-vous doit être pris au moins 30 minutes à l'avance");
        }
       
        int duration;
        String providerType;
        long providerId;
       
        if (appointment.getBarber() != null) {
            providerType = "barber";
            providerId = appointment.getBarber().getId();
            duration = appointment.getHaircut().getDuration();
        } else if (appointment.getEsthetician() != null) {
            providerType = "esthetician";
            providerId = appointment.getEsthetician().getId();
            duration = appointment.getEsthetic().getDuration();
        } else {
            throw new IllegalStateException("Le rendez-vous doit avoir soit un barbier soit une esthéticienne");
        }
       
        LocalDateTime appointmentTime = appointment.getAppointmentTime();
        availabilityService.makeProviderSlotUnavailable(providerType, providerId, appointmentTime, duration);
    }
  
    return appointment;
}

}

/*
    PENDING(1),
    REQUESTED(2),
    CONFIRMED(3),
    CHECK_IN(4),
    IN_PROGRESS(5),
    COMPLETED(6),
    CANCELLED_BY_CLIENT(7),
    CANCELLED_BY_PROVIDER(8),
    RESCHEDULED(9),
    NO_SHOW(10);
 */