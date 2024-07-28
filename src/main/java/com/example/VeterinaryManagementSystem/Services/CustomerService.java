package com.example.VeterinaryManagementSystem.Services;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.example.VeterinaryManagementSystem.Dto.Request.CustomerRequest;
import com.example.VeterinaryManagementSystem.Dto.Response.CustomerResponse;
import com.example.VeterinaryManagementSystem.Entity.Customer;
import com.example.VeterinaryManagementSystem.Exception.DuplicateDataException;
import com.example.VeterinaryManagementSystem.Exception.EntityAlreadyExistException;
import com.example.VeterinaryManagementSystem.Exception.EntityNotFoundException;
import com.example.VeterinaryManagementSystem.Repository.CustomerRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final ModelMapper modelMapper;

    public Page<CustomerResponse> findAllCustomers(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<Customer> customersPage = customerRepository.findAll(pageable);
        if (customersPage.isEmpty()) {
            throw new EntityNotFoundException("No customers found.");
        }
        return customersPage.map(customer -> modelMapper.map(customer, CustomerResponse.class));
    }

    public Customer findCustomerById(Long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(id, Customer.class));
    }

    public CustomerResponse findCustomerResponseById(Long id) {
        Customer customer = findCustomerById(id);
        return modelMapper.map(customer, CustomerResponse.class);
    }

    public Page<CustomerResponse> findCustomersByName(String name, int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<Customer> customersPage = customerRepository.findByNameContaining(name, pageable);
        if (customersPage.isEmpty()) {
            throw new EntityNotFoundException("No customers found with name: " + name);
        }
        return customersPage.map(customer -> modelMapper.map(customer, CustomerResponse.class));
    }

    public CustomerResponse createCustomer(CustomerRequest customerRequest) {
        Optional<Customer> existCustomerWithSameSpecs = customerRepository.findByNameAndEmail(customerRequest.getName(), customerRequest.getEmail());

        if (existCustomerWithSameSpecs.isPresent()) {
            throw new EntityAlreadyExistException(Customer.class);
        }
        Customer newCustomer = modelMapper.map(customerRequest, Customer.class);
        return modelMapper.map(customerRepository.save(newCustomer), CustomerResponse.class);
    }

    public CustomerResponse updateCustomer(Long id, CustomerRequest customerRequest) {
        Customer customerFromDb = findCustomerById(id);
        Optional<Customer> existOtherCustomerFromRequest = customerRepository.findByNameAndEmail(customerRequest.getName(), customerRequest.getEmail());

        if (existOtherCustomerFromRequest.isPresent() && !existOtherCustomerFromRequest.get().getId().equals(id)) {
            throw new DuplicateDataException(Customer.class);
        }

        modelMapper.map(customerRequest, customerFromDb);
        return modelMapper.map(customerRepository.save(customerFromDb), CustomerResponse.class);
    }

    public String deleteCustomer(Long id) {
        Customer customerFromDb = findCustomerById(id);
        customerRepository.delete(customerFromDb);
        return "Customer deleted.";
    }
}