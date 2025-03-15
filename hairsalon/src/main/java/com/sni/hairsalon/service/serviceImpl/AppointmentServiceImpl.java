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
import com.sni.hairsalon.model.Haircut;
import com.sni.hairsalon.repository.AppointmentRepository;
import com.sni.hairsalon.repository.BarberRepository;
import com.sni.hairsalon.repository.ClientRepository;
import com.sni.hairsalon.repository.HaircutRepository;
import com.sni.hairsalon.repository.UserRepository;
import com.sni.hairsalon.security.filter.JWTFilter;
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
  private final ClientRepository clientRepo;

  @Transactional
  @Override
  public AppointmentResponseDTO createAppointment(AppointmentRequestDTO request) {

    User user = userRepo.findUserByEmail(request.getEmail())
        .orElseThrow(() -> new ResourceNotFoundException("User not Found"));

    Client client = clientRepo.findClientByUserID(user.getId())
        .orElseThrow(() -> new ResourceNotFoundException("client not found"));

    Barber barber = barberRepo.findById(Long.parseLong(request.getBarberId()))
        .orElseThrow(() -> new ResourceNotFoundException("Barber not found"));

    Haircut haircut = findHaircutByTypeFlexible(request.getHaircutType());

    LocalDate appointmentDate = request.getAppointmentTime().toLocalDate();
    LocalDateTime appointmentDateTime = request.getAppointmentTime();
    LocalTime appointmentTime = request.getAppointmentTime().toLocalTime();

    ScheduleResponseDTO schedule = scheduleService.getBarberScheduleForDate(Long.parseLong(request.getBarberId()),
        appointmentDate);

    LocalTime startSchedule = schedule.getStartTime().toLocalTime();
    LocalTime endSchedule = schedule.getEndTime().toLocalTime();

    if (appointmentTime.isBefore(startSchedule)
        || appointmentTime.isAfter(endSchedule)) {
      throw new IllegalStateException("The appointment time doesn't match the barber hour" +
      "startTime: " + schedule.getStartTime() + " endTime: " + schedule.getEndTime());
    }
     //TODO reuse it after fixing the function in availability

    // if (!availabilityService.isAvailableSlot(barber.getId(),
    //     appointmentTime, haircut.getDuration())) {

    //   throw new IllegalStateException("Slot is not available");
    // }

    availabilityService.makeSlotUnavailable(barber.getId(), appointmentDateTime, haircut.getDuration());

    Appointment appointment = Appointment.builder()
        .client(client)
        .barber(barber)
        .haircut(haircut)
        .appointmentTime(appointmentDateTime)
        .status(Status.CONFIRMED.getCode())
        .build();
    Appointment verifiedAppointment = checkAppointmentTodayDate(appointment);
    appointmentRepo.save(verifiedAppointment);
    AppointmentResponseDTO response = mapper.toDto(appointment);
    mailService.sendAppointmentConfirmation(client.getUser().getEmail(), response);
    mailService.sendBarberNotificationOfNewAppointment(response.getBarberEmail(), verifiedAppointment);
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

  @Override
  public AppointmentResponseDTO updateAppointmentByAdmin(long id,AppointmentUpdateRequestDTO request){

    Appointment appointment = appointmentRepo.findById(id)
    .orElseThrow(() -> new ResourceNotFoundException("Appointment not found"));

  User user = userRepo.findUserByEmail(request.getEmail())
    .orElseThrow(() -> new ResourceNotFoundException("User not Found"));

  Client client = clientRepo.findClientByUserID(user.getId())
    .orElseThrow(() -> new ResourceNotFoundException("Client not found"));

  Barber barber = barberRepo.findById(Long.parseLong(request.getBarberId()))
    .orElseThrow(() -> new ResourceNotFoundException("Barber not found"));

  Haircut haircut = findHaircutByTypeFlexible(request.getHaircutType());

  LocalDate appointmentDate = request.getAppointmentTime().toLocalDate();
  LocalDateTime appointmentDateTime = request.getAppointmentTime();
  LocalTime appointmentTime = request.getAppointmentTime().toLocalTime();

  ScheduleResponseDTO schedule = scheduleService.getBarberScheduleForDate(Long.parseLong(request.getBarberId()),
  appointmentDate);

LocalTime startSchedule = schedule.getStartTime().toLocalTime();
LocalTime endSchedule = schedule.getEndTime().toLocalTime();

if (appointmentTime.isBefore(startSchedule)
  || appointmentTime.isAfter(endSchedule)) {
throw new IllegalStateException("The appointment time doesn't match the barber hour" +
"startTime: " + schedule.getStartTime() + " endTime: " + schedule.getEndTime());
}

  // //TODO: after fixing the function in the availability service reuse those here
  // if (!availabilityService.isAvailableSlot(barber.getId(),
  //   appointmentTime, haircut.getDuration())) {

  //   throw new IllegalStateException("Slot is not available");
  // }

  availabilityService.makeSlotUnavailable(barber.getId(), appointmentDateTime, haircut.getDuration());

  appointment.setClient(client);
  appointment.setBarber(barber);
  appointment.setHaircut(haircut);
  appointment.setAppointmentTime(appointmentDateTime);
  appointment.setStatus(Status.CONFIRMED.getCode());

  appointmentRepo.save(appointment);

  AppointmentResponseDTO response = mapper.toDto(appointment);
  mailService.sendAppointmentConfirmation(client.getUser().getEmail(), response);
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
    public AppointmentResponseDTO checkIn(String email){

      Client client =  clientRepo.findByEmail(email)
      .orElseThrow(()->new ResourceNotFoundException("Client not found"));
      LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES).plusHours(1);
      LocalDate currentDate = now.toLocalDate();
      //find all appointments for client for the current day
      List<Appointment> appointments = appointmentRepo.findByClientAndDateAndStatus(client.getId(),
       currentDate, Status.CONFIRMED.getCode());

      if(appointments.isEmpty()){

        throw new ResourceNotFoundException("No confirmed appointment found for the day");
      }

      //Get the closest appointment with appointment time
      Appointment appointment = appointments.stream()
      .min(Comparator.comparing(Appointment::getAppointmentTime))
      .orElseThrow(()-> new IllegalStateException("No appointment retrieve"));
      
      LocalDateTime appointmentTime = appointment.getAppointmentTime();
      
      //verify if the checking is done before 16 minutes earlier than the appointment time
      validateCheckInTime(now, appointmentTime);

      //update status and save the apt
      appointment.setStatus(Status.CHECK_IN.getCode());
      appointmentRepo.save(appointment);
      notifyBarber(appointment);

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
  public void cancelAppointment(long id) {

    Appointment cancelAppointment = appointmentRepo.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Appointment not found"));
    cancelAppointment.setStatus(Status.CANCELLED_BY_PROVIDER.getCode());
    appointmentRepo.save(cancelAppointment);

    return;
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
  @Scheduled(fixedRate = 30000)
  public int countAppointmentForTheDay() {

    List<AppointmentResponseDTO> appointments = getAllBarberAppointment(LocalDate.now());

    return appointments.size();
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

  private void notifyBarber(Appointment apt) {
    mailService.sendCheckIn(apt);
    return;
  }
  
  public List<AppointmentResponseDTO> getMyBarberAppointment(String email){

    Barber barber = barberRepo.findByEmail(email)
    .orElseThrow(()-> new ResourceNotFoundException("Client not found"));

    return getBarberAppointment(barber.getId());

  }

  private void makeInProgressToCompleted(Appointment appointment, LocalDateTime now){

    if(appointment.getAppointmentTime().isAfter(now.plusMinutes(10))){

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

    if(appointment.getAppointmentTime().toLocalDate().equals(now.toLocalDate())){
      
      LocalDateTime eveningStart = now.toLocalDate().atTime(17,00);

      if(now.isAfter(eveningStart.minusHours(4))){
        
        throw new IllegalStateException("Appointment taken the same day must be 4 hours prior to 17h00");
      } 

      Barber barber = appointment.getBarber();
      int duration = appointment.getHaircut().getDuration();

      //We look for slot available betweeen 17h-19h for the date of the appointment
      LocalDateTime startSlot = eveningStart;
      LocalDateTime eveningEnd = now.toLocalDate().atTime(19,00);
      
      boolean slotFound = false;

      while(startSlot.plusMinutes(duration).isBefore(eveningEnd) ||
      startSlot.isBefore(eveningEnd)){

        //TODO reuse it after fixing the function in availability
        // if(availabilityService.isAvailableSlot(barber.getId(), startSlot, duration)){

        //   appointment.setAppointmentTime(startSlot);
        //   availabilityService.makeSlotUnavailable(barber.getId(), startSlot, duration);
        //   slotFound = true;
        // }

        if(slotFound == false){
          throw new ResourceNotFoundException("No slot found for 17h to 19h");
        }

      }
      
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