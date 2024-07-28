package com.example.VeterinaryManagementSystem.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import com.example.VeterinaryManagementSystem.Dto.Request.WorkDayRequest;
import com.example.VeterinaryManagementSystem.Dto.Response.WorkDayResponse;
import com.example.VeterinaryManagementSystem.Services.WorkDayService;

@RestController
@RequestMapping("/workdays")
@RequiredArgsConstructor
public class WorkDayController {

    private final WorkDayService workDayService;

    @GetMapping
    public Page<WorkDayResponse> getAllWorkDays(@RequestParam int pageNumber, @RequestParam int pageSize) {
        return workDayService.findAllWorkDays(pageNumber, pageSize);
    }

    @GetMapping("/{id}")
    public WorkDayResponse getWorkDayById(@PathVariable Long id) {
        return workDayService.findWorkDayById(id);
    }

    @PostMapping
    public WorkDayResponse createWorkDay(@RequestBody WorkDayRequest workDayRequest) {
        return workDayService.createWorkDay(workDayRequest);
    }

    @PutMapping("/{id}")
    public WorkDayResponse updateWorkDay(@PathVariable Long id, @RequestBody WorkDayRequest workDayRequest) {
        return workDayService.updateWorkDay(id, workDayRequest);
    }

    @DeleteMapping("/{id}")
    public String deleteWorkDay(@PathVariable Long id) {
        return workDayService.deleteWorkDay(id);
    }
}
