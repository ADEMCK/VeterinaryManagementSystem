package com.example.VeterinaryManagementSystem.Controller;

import com.example.VeterinaryManagementSystem.Dto.Request.CustomerRequest;
import com.example.VeterinaryManagementSystem.Dto.Response.CustomerResponse;
import com.example.VeterinaryManagementSystem.Services.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @GetMapping
    public Page<CustomerResponse> getAllCustomers(@RequestParam int page, @RequestParam int size) {
        return customerService.findAllCustomers(page, size);
    }

    @GetMapping("/{id}")
    public CustomerResponse getCustomerById(@PathVariable Long id) {
        return customerService.findCustomerResponseById(id);
    }

    @GetMapping("/search")
    public Page<CustomerResponse> searchCustomersByName(@RequestParam String name, @RequestParam int page, @RequestParam int size) {
        return customerService.findCustomersByName(name, page, size);
    }

    @PostMapping
    public CustomerResponse createCustomer(@RequestBody CustomerRequest customerRequest) {
        return customerService.createCustomer(customerRequest);
    }

    @PutMapping("/{id}")
    public CustomerResponse updateCustomer(@PathVariable Long id, @RequestBody CustomerRequest customerRequest) {
        return customerService.updateCustomer(id, customerRequest);
    }

    @DeleteMapping("/{id}")
    public String deleteCustomer(@PathVariable Long id) {
        return customerService.deleteCustomer(id);
    }
}
