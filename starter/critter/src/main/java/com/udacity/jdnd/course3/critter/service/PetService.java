package com.udacity.jdnd.course3.critter.service;

import com.udacity.jdnd.course3.critter.entity.Customer;
import com.udacity.jdnd.course3.critter.entity.Pet;
import com.udacity.jdnd.course3.critter.exception.ObjectNotFoundException;
import com.udacity.jdnd.course3.critter.repository.PetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

import static java.util.Collections.emptyList;
import static java.util.Objects.nonNull;
import static java.util.Optional.ofNullable;

@Transactional
@Service
public class PetService {

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private CustomerService customerService;

    public List<Pet> findAllPets() {
        return ofNullable(this.petRepository.findAll()).orElse(emptyList());
    }

    public List<Pet> findAllPetsByIds(List<Long> petIds) {
        return ofNullable(this.petRepository.findAllById(ofNullable(petIds).orElse(emptyList()))).orElse(emptyList());
    }

    public Pet findPetById(Long petId) {
        return this.petRepository.findById(petId)
                .orElseThrow(() -> new ObjectNotFoundException("Pet ID" + petId + "does not exist"));
    }

    public List<Pet> findAllPetsByOwnerId(Long ownerId) {
        return ofNullable(this.customerService.findCustomerById(ownerId)).map(Customer::getPets).orElse(emptyList());
    }

    public Pet savePet(Pet pet) {
        Customer owner = ofNullable(pet).map(Pet::getOwner)
                .orElseThrow(() -> new ObjectNotFoundException("Pet does not exist"));
        if(nonNull(owner)) {
            List<Pet> pets = ofNullable(owner.getPets()).orElse(emptyList());
            pets.add(pet);
            owner.setPets(pets);
        }
        return this.petRepository.save(pet);
    }

}