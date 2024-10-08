package com.example.VeterinaryManagementSystem.Services;

import com.example.VeterinaryManagementSystem.Repository.AnimalRepository;
import com.example.VeterinaryManagementSystem.Repository.DoctorRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.example.VeterinaryManagementSystem.Dto.Request.VaccinationRequest;
import com.example.VeterinaryManagementSystem.Dto.Response.VaccinationResponse;
import com.example.VeterinaryManagementSystem.Entity.Vaccination;
import com.example.VeterinaryManagementSystem.Exception.DuplicateDataException;
import com.example.VeterinaryManagementSystem.Exception.EntityNotFoundException;
import com.example.VeterinaryManagementSystem.Exception.ProtectionStillActiveException;
import com.example.VeterinaryManagementSystem.Repository.VaccinationRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VaccinationService {

    private final VaccinationRepository vaccinationRepository;
    private final ReportService reportService;
    private final ModelMapper modelMapper;
    private  final AnimalRepository animalRepository;

    public Page<VaccinationResponse> findAllVaccinations(int pageNumber, int pageSize) {

        Pageable pageable = PageRequest.of(pageNumber, pageSize);

        return vaccinationRepository.findAll(pageable).map(vaccination -> modelMapper.map(vaccination, VaccinationResponse.class));
    }

    public VaccinationResponse findVaccinationById(Long id) {

        return modelMapper.map(vaccinationRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(id, Vaccination.class))
                , VaccinationResponse.class);
    }

    public Page<VaccinationResponse> findAnimalsByVaccinationProtectionFinishDateRange(LocalDate startDate, LocalDate endDate, int pageNumber, int pageSize) {

        Pageable pageable = PageRequest.of(pageNumber, pageSize);

        return vaccinationRepository.findByProtectionFinishDateBetween(startDate, endDate, pageable).map(vaccination -> modelMapper.map(vaccination, VaccinationResponse.class));
    }

    public Page<VaccinationResponse> findVaccinationsByAnimal(Long id, int pageNumber, int pageSize) {

        Pageable pageable = PageRequest.of(pageNumber, pageSize);

        return vaccinationRepository.findByAnimalId(id, pageable).map(vaccination -> modelMapper.map(vaccination, VaccinationResponse.class));
    }

    public VaccinationResponse createVaccination(VaccinationRequest vaccinationRequest) {
        List<Vaccination> existValidVaccinationWithSameSpecsAnd =
                vaccinationRepository.findByNameAndCodeAndAnimalIdAndProtectionFinishDateGreaterThanEqual(vaccinationRequest.getName(), vaccinationRequest.getCode(), (long) vaccinationRequest.getAnimalId(),vaccinationRequest.getProtectionStartDate());

        if (!existValidVaccinationWithSameSpecsAnd.isEmpty()) {
            throw new ProtectionStillActiveException("The vaccine you want to save is still protective for this animal.");
        }

        Vaccination newVaccination = modelMapper.map(vaccinationRequest, Vaccination.class);
        newVaccination.setAnimal(animalRepository.findById(vaccinationRequest.getAnimalId()).orElseThrow());
        //newVaccination.setReport(reportService.findReportById(vaccinationRequest.getReportId()));
        return modelMapper.map(vaccinationRepository.save(newVaccination), VaccinationResponse.class);
    }

    public VaccinationResponse updateVaccination(Long id, VaccinationRequest vaccinationRequest) {
        Optional<Vaccination> vaccinationFromDb = vaccinationRepository.findById(id);
        List<Vaccination> existOtherValidVaccinationFromRequest =
                vaccinationRepository.findByNameAndCodeAndAnimalIdAndProtectionFinishDateGreaterThanEqual(vaccinationRequest.getName(), vaccinationRequest.getCode(), vaccinationRequest.getAnimalId(), vaccinationRequest.getProtectionStartDate());

        if (vaccinationFromDb.isEmpty()) {
            throw new EntityNotFoundException(id, Vaccination.class);
        }

        if (!existOtherValidVaccinationFromRequest.isEmpty() && !existOtherValidVaccinationFromRequest.get(existOtherValidVaccinationFromRequest.size() - 1).getId().equals(id)) {
            throw new DuplicateDataException(Vaccination.class);
        }

        if (!existOtherValidVaccinationFromRequest.isEmpty()) {
            throw new ProtectionStillActiveException("The vaccine you want to update is still protective for this animal.");
        }

        Vaccination updatedVaccination = vaccinationFromDb.get();
        modelMapper.map(vaccinationRequest, updatedVaccination);
        return modelMapper.map(vaccinationRepository.save(updatedVaccination), VaccinationResponse.class);
    }

    public String deleteVaccination(Long id) {
        Optional<Vaccination> vaccinationFromDb = vaccinationRepository.findById(id);
        if (vaccinationFromDb.isEmpty()) {
            throw new EntityNotFoundException(id, Vaccination.class);
        } else {
            vaccinationRepository.delete(vaccinationFromDb.get());
            return "Vaccine deleted.";
        }
    }

    public List<Vaccination> getVaccinationsByAnimal(Long animalId) {
        return vaccinationRepository.findByAnimalId(animalId);
    }

}