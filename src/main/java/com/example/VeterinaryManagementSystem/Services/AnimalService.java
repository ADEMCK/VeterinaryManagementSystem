package com.example.VeterinaryManagementSystem.Services;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.example.VeterinaryManagementSystem.Dto.Request.AnimalRequest;
import com.example.VeterinaryManagementSystem.Dto.Response.AnimalResponse;
import com.example.VeterinaryManagementSystem.Entity.Animal;
import com.example.VeterinaryManagementSystem.Entity.Customer;
import com.example.VeterinaryManagementSystem.Exception.DuplicateDataException;
import com.example.VeterinaryManagementSystem.Exception.EntityAlreadyExistException;
import com.example.VeterinaryManagementSystem.Exception.EntityNotFoundException;
import com.example.VeterinaryManagementSystem.Repository.AnimalRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AnimalService {

    private final AnimalRepository animalRepository;
    private final ModelMapper modelMapper;
    private final CustomerService customerService;

    public Page<AnimalResponse> findAllAnimals(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return animalRepository.findAll(pageable).map(animal -> modelMapper.map(animal, AnimalResponse.class));
    }

    public AnimalResponse findAnimalById(Long id) {
        return modelMapper.map(animalRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(id, Animal.class)),
                AnimalResponse.class);
    }

    public Page<AnimalResponse> findAnimalsByName(String name, int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return animalRepository.findByNameContaining(name, pageable).map(animal -> modelMapper.map(animal, AnimalResponse.class));
    }

    public Page<AnimalResponse> findAnimalsByCustomer(String customerName, int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return animalRepository.findByCustomerName(customerName, pageable).map(animal -> modelMapper.map(animal, AnimalResponse.class));
    }

    public AnimalResponse createAnimal(AnimalRequest animalRequest) {
        Long customerId = animalRequest.getCustomerId();
        Customer customerFromDb = customerService.findCustomerById(customerId);

        Optional<Animal> existAnimalWithSameSpecs = animalRepository.findByNameAndSpeciesAndGenderAndDateOfBirth(
                animalRequest.getName(), animalRequest.getSpecies(), animalRequest.getGender(), animalRequest.getDateOfBirth());

        if (existAnimalWithSameSpecs.isPresent()) {
            throw new EntityAlreadyExistException(Animal.class);
        }

        Animal newAnimal = modelMapper.map(animalRequest, Animal.class);
        newAnimal.setCustomer(customerFromDb);

        return modelMapper.map(animalRepository.save(newAnimal), AnimalResponse.class);
    }

    public AnimalResponse updateAnimal(Long id, AnimalRequest animalRequest) {
        Optional<Animal> animalFromDb = animalRepository.findById(id);
        Optional<Animal> existOtherAnimalFromRequest = animalRepository.findByNameAndSpeciesAndGenderAndDateOfBirth(
                animalRequest.getName(), animalRequest.getSpecies(), animalRequest.getGender(), animalRequest.getDateOfBirth());

        if (animalFromDb.isEmpty()) {
            throw new EntityNotFoundException(id, Animal.class);
        }

        if (existOtherAnimalFromRequest.isPresent() && !existOtherAnimalFromRequest.get().getId().equals(id)) {
            throw new DuplicateDataException(Animal.class);
        }

        Animal updatedAnimal = animalFromDb.get();
        modelMapper.map(animalRequest, updatedAnimal);
        updatedAnimal.setCustomer(customerService.findCustomerById(animalRequest.getCustomerId()));

        return modelMapper.map(animalRepository.save(updatedAnimal), AnimalResponse.class);
    }

    public String deleteAnimal(Long id) {
        Optional<Animal> animalFromDb = animalRepository.findById(id);
        if (animalFromDb.isEmpty()) {
            throw new EntityNotFoundException(id, Animal.class);
        } else {
            animalRepository.delete(animalFromDb.get());
            return "Animal deleted.";
        }
    }
}