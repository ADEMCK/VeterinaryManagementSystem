package com.example.VeterinaryManagementSystem.Services;

import com.example.VeterinaryManagementSystem.Dto.Response.AppointmentForReportResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.example.VeterinaryManagementSystem.Dto.Request.ReportRequest;
import com.example.VeterinaryManagementSystem.Dto.Response.ReportResponse;
import com.example.VeterinaryManagementSystem.Entity.Appointment;
import com.example.VeterinaryManagementSystem.Entity.Report;
import com.example.VeterinaryManagementSystem.Exception.DuplicateDataException;
import com.example.VeterinaryManagementSystem.Exception.EntityAlreadyExistException;
import com.example.VeterinaryManagementSystem.Exception.EntityNotFoundException;
import com.example.VeterinaryManagementSystem.Repository.ReportRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final ReportRepository reportRepository;
    private final AppointmentService appointmentService;
    private final ModelMapper modelMapper;

    public Page<ReportResponse> findAllReports(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return reportRepository.findAll(pageable).map(this::reportResponseDtoFromReport);
    }

    public ReportResponse findReportById(Long id) {
        return reportRepository.findById(id).map(this::reportResponseDtoFromReport).orElseThrow(() -> new EntityNotFoundException(id, Report.class));
    }

    public ReportResponse createReport(ReportRequest reportRequest) {
        Optional<Report> existReportWithSameSpecs = reportRepository.findByAppointmentId(reportRequest.getAppointmentId());

        if (existReportWithSameSpecs.isPresent()) {
            throw new EntityAlreadyExistException(Report.class);
        }

        Appointment appointmentFromDb = appointmentService.findAppointmenById(reportRequest.getAppointmentId());
        reportRequest.setAppointmentId(null);
        Report newReport = modelMapper.map(reportRequest, Report.class);
        newReport.setAppointment(appointmentFromDb);
        return reportResponseDtoFromReport(reportRepository.save(newReport));
    }

    public ReportResponse updateReport(Long id, ReportRequest reportRequest) {
        Optional<Report> reportFromDb = reportRepository.findById(id);
        Optional<Report> existOtherReportFromRequest =
                reportRepository.findReportByTitleAndDiagnosisAndPriceAndAppointmentId(reportRequest.getTitle(), reportRequest.getDiagnosis(), reportRequest.getPrice(), reportRequest.getAppointmentId());

        if (reportFromDb.isEmpty()) {
            throw new EntityNotFoundException(id, Report.class);
        }

        if (existOtherReportFromRequest.isPresent() && !existOtherReportFromRequest.get().getId().equals(id)) {
            throw new DuplicateDataException(Report.class);
        }

        Appointment appointmentFromDb = appointmentService.findAppointmenById(reportRequest.getAppointmentId());
        Report updatedReport = reportFromDb.get();
        updatedReport.setTitle(reportRequest.getTitle());
        updatedReport.setDiagnosis(reportRequest.getDiagnosis());
        updatedReport.setPrice(reportRequest.getPrice());
        updatedReport.setAppointment(appointmentFromDb);
        return reportResponseDtoFromReport(reportRepository.save(updatedReport));
    }

    public String deleteReport(Long id) {
        Optional<Report> reportFromDb = reportRepository.findById(id);
        if (reportFromDb.isEmpty()) {
            throw new EntityNotFoundException(id, Report.class);
        } else {
            reportRepository.delete(reportFromDb.get());
            return "Report deleted.";
        }
    }

    public ReportResponse reportResponseDtoFromReport(Report report) {
        ReportResponse reportResponse = modelMapper.map(report, ReportResponse.class);
        reportResponse.getAppointment().setCustomerName(report.getAppointment().getAnimal().getCustomer().getName());
        return reportResponse;
    }
}