package com.example.VeterinaryManagementSystem.Services;

import com.example.VeterinaryManagementSystem.Repository.AnimalRepository;
import com.example.VeterinaryManagementSystem.Repository.CustomerRepository;
import com.example.VeterinaryManagementSystem.Repository.DoctorRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.example.VeterinaryManagementSystem.Dto.Request.AppointmentRequest;
import com.example.VeterinaryManagementSystem.Dto.Response.AppointmentResponse;
import com.example.VeterinaryManagementSystem.Entity.Appointment;
import com.example.VeterinaryManagementSystem.Entity.WorkDay;
import com.example.VeterinaryManagementSystem.Exception.DoctorAppointmentConflictException;
import com.example.VeterinaryManagementSystem.Exception.DoctorNotAvailableException;
import com.example.VeterinaryManagementSystem.Exception.EntityAlreadyExistException;
import com.example.VeterinaryManagementSystem.Exception.EntityNotFoundException;
import com.example.VeterinaryManagementSystem.Repository.AppointmentRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final WorkDayService workDayService;
    private final ModelMapper modelMapper;
    private  final AnimalRepository animalRepository;
    private  final DoctorRepository doctorRepository;

    public Page<AppointmentResponse> findAllAppointments(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return appointmentRepository.findAll(pageable).map(appointment -> modelMapper.map(appointment, AppointmentResponse.class));
    }

    public AppointmentResponse findAppointmentByIdResponse(Long id) {
        return modelMapper.map(appointmentRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(id, Appointment.class))
                , AppointmentResponse.class);
    }

    public Appointment findAppointmenById(Long id) {
        return appointmentRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(id, Appointment.class));
    }

    public Page<AppointmentResponse> findAppointmentByDoctorIdAndDateRange(Long doctorId, LocalDate startDate, LocalDate endDate, int pageNumber, int pageSize) {

        Pageable pageable = PageRequest.of(pageNumber, pageSize);

        if (doctorId == null) {
            return appointmentRepository.findByAppointmentDateBetween(startDate.atStartOfDay(), endDate.atStartOfDay(), pageable).map(appointment -> modelMapper.map(appointment, AppointmentResponse.class));
        }
        return appointmentRepository.findByDoctorIdAndAppointmentDateBetween(doctorId, startDate.atStartOfDay(), endDate.atStartOfDay(), pageable).map(appointment -> modelMapper.map(appointment, AppointmentResponse.class));
    }

    public Page<AppointmentResponse> findAppointmentByAnimalIdAndDateRange(Long animalId, LocalDate startDate, LocalDate endDate, int pageNumber, int pageSize) {

        Pageable pageable = PageRequest.of(pageNumber, pageSize);

        if (animalId == null) {
            return appointmentRepository.findByAppointmentDateBetween(startDate.atStartOfDay(), endDate.atStartOfDay(), pageable).map(appointment -> modelMapper.map(appointment, AppointmentResponse.class));
        }
        return appointmentRepository.findByAnimalIdAndAppointmentDateBetween(animalId, startDate.atStartOfDay(), endDate.atStartOfDay(), pageable).map(appointment -> modelMapper.map(appointment, AppointmentResponse.class));
    }

    public AppointmentResponse createAppointment(AppointmentRequest appointmentRequest) {

        Optional<Appointment> existAppointmentWithSameSpecs =
                appointmentRepository.findByAppointmentDateAndDoctorIdAndAnimalId(appointmentRequest.getAppointmentDate(), appointmentRequest.getDoctor_id(), appointmentRequest.getAnimal_id());

        Optional<WorkDay> existsWorkDayByDoctorIdAndDate =
                workDayService.findByDoctorIdAndDate(appointmentRequest.getDoctor_id(), appointmentRequest.getAppointmentDate().toLocalDate());

        Optional<Appointment> existAppointmentWithDateAndDoctorId =
                appointmentRepository.findByAppointmentDateAndDoctorId(appointmentRequest.getAppointmentDate(), appointmentRequest.getDoctor_id());

        if (existAppointmentWithSameSpecs.isPresent()) {
            throw new EntityAlreadyExistException(Appointment.class);
        }

        if (existsWorkDayByDoctorIdAndDate.isEmpty()) {
            throw new DoctorNotAvailableException(appointmentRequest.getAppointmentDate().toLocalDate());
        }

        if (existAppointmentWithDateAndDoctorId.isPresent()) {
            throw new DoctorAppointmentConflictException(appointmentRequest.getAppointmentDate().toLocalDate());
        }
        LocalDateTime atStart = appointmentRequest.getAppointmentDate().truncatedTo(ChronoUnit.HOURS);
        LocalDateTime atEnd = appointmentRequest.getAppointmentDate().truncatedTo(ChronoUnit.HOURS).plus(1,ChronoUnit.HOURS);
        Pageable pageable = PageRequest.of(1, 50);
        Page<Appointment> byDoctorIdAndAppointmentDateBetween = appointmentRepository.findByDoctorIdAndAppointmentDateBetween(appointmentRequest.getDoctor_id(), atStart, atEnd, pageable);
        if(!byDoctorIdAndAppointmentDateBetween.isEmpty()){
            throw new DoctorAppointmentConflictException(appointmentRequest.getAppointmentDate().toLocalDate());
        }
        Appointment newAppointment = modelMapper.map(appointmentRequest, Appointment.class);
        newAppointment.setAnimal(animalRepository.findById(appointmentRequest.getAnimal_id()).orElseThrow());
        newAppointment.setDoctor(doctorRepository.findById(appointmentRequest.getDoctor_id()).orElseThrow());

        return modelMapper.map(appointmentRepository.save(newAppointment), AppointmentResponse.class);
    }

    public AppointmentResponse updateAppointment(Long id, AppointmentRequest appointmentRequest) {

        Optional<Appointment> appointmentFromDb = appointmentRepository.findById(id);

        if (appointmentFromDb.isEmpty()) {
            throw new EntityNotFoundException(id, Appointment.class);
        }

        Optional<Appointment> existAppointmentWithSameSpecs =
                appointmentRepository.findByAppointmentDateAndDoctorIdAndAnimalId(appointmentRequest.getAppointmentDate(), appointmentRequest.getDoctor_id(), appointmentRequest.getAnimal_id());

        if (existAppointmentWithSameSpecs.isPresent()) {
            throw new EntityAlreadyExistException(Appointment.class);
        }

        Optional<WorkDay> existsWorkDayByDoctorIdAndDate =
                workDayService.findByDoctorIdAndDate(appointmentRequest.getDoctor_id(), appointmentRequest.getAppointmentDate().toLocalDate());

        if (existsWorkDayByDoctorIdAndDate.isEmpty()) {
            throw new DoctorNotAvailableException(appointmentRequest.getAppointmentDate().toLocalDate());
        }

        Optional<Appointment> existAppointmentWithDateAndDoctorId =
                appointmentRepository.findByAppointmentDateAndDoctorId(appointmentRequest.getAppointmentDate(), appointmentRequest.getDoctor_id());

        if (existAppointmentWithDateAndDoctorId.isPresent()) {
            throw new DoctorAppointmentConflictException(appointmentRequest.getAppointmentDate().toLocalDate());
        }

        Appointment updatedAppointment = appointmentFromDb.get();
        modelMapper.map(appointmentRequest, updatedAppointment);
        return modelMapper.map(appointmentRepository.save(updatedAppointment), AppointmentResponse.class);
    }

    public String deleteAppointment(Long id) {
        Optional<Appointment> appointmentFromDb = appointmentRepository.findById(id);

        if (appointmentFromDb.isEmpty()) {
            throw new EntityNotFoundException(id, Appointment.class);
        } else {
            appointmentRepository.delete(appointmentFromDb.get());
            return "Appointment deleted.";
        }
    }
    /* public boolean isDoctorAvailable(Long doctorId, LocalDateTime appointmentTime) {
        List<Appointment> appointments = appointmentRepository.findByDoctorIdAndAppointmentTime(doctorId, appointmentTime);
        return appointments.isEmpty();
    }
    public List<Appointment> getAppointmentsByDateRangeAndAnimal(LocalDate startDate, LocalDate endDate, Long animalId) {
        return appointmentRepository.findByDateRangeAndAnimalId(startDate, endDate, animalId);
    }
    public List<Appointment> getAppointmentsByDateRangeAndDoctor(LocalDate startDate, LocalDate endDate, Long doctorId) {
        return appointmentRepository.findByDateRangeAndDoctorId(startDate, endDate, doctorId);
    } */

}