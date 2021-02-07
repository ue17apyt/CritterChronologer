package com.udacity.jdnd.course3.critter.service;

import com.udacity.jdnd.course3.critter.entity.Pet;
import com.udacity.jdnd.course3.critter.exception.ObjectNotFoundException;
import com.udacity.jdnd.course3.critter.repository.PetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
@Service
public class PetService {

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private CustomerService customerService;

    public List<Pet> findAllPets() {
        return this.petRepository.findAll();
    }

    public List<Pet> findAllPetsByIds(List<Long> petIds) {
        return this.petRepository.findAllById(petIds);
    }

    public Pet findPetById(Long petId) {
        return this.petRepository.findById(petId)
                .orElseThrow(() -> new ObjectNotFoundException("Pet ID" + petId + "does not exist"));
    }

    public List<Pet> findAllPetsByOwnerId(Long ownerId) {
        return this.petRepository.findByOwnerId(ownerId);
    }

    public Pet savePet(Pet pet) {
        return this.petRepository.save(pet);
    }

}