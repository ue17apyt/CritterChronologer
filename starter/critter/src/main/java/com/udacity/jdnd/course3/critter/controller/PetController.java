package com.udacity.jdnd.course3.critter.controller;

import com.udacity.jdnd.course3.critter.dto.PetDTO;
import com.udacity.jdnd.course3.critter.entity.Customer;
import com.udacity.jdnd.course3.critter.entity.Pet;
import com.udacity.jdnd.course3.critter.service.CustomerService;
import com.udacity.jdnd.course3.critter.service.PetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static java.util.Collections.emptyList;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static org.springframework.beans.BeanUtils.copyProperties;

/**
 * Handles web requests related to Pets.
 */
@RestController
@RequestMapping("/pet")
public class PetController {

    @Autowired
    private PetService petService;

    @Autowired
    private CustomerService customerService;

    @PostMapping
    public PetDTO savePet(@RequestBody PetDTO petDTO) {
        Pet pet = this.petService.savePet(convertPetDtoToPet(petDTO));
        if (petDTO.getOwnerId() != 0L) {
            Customer owner = this.customerService.findCustomerById(petDTO.getOwnerId());
            List<Pet> pets = ofNullable(owner.getPets()).orElse(emptyList());
            pets.add(pet);
            owner.setPets(pets);
            this.customerService.saveCustomer(owner);
        }
        petDTO.setId(pet.getId());
        return petDTO;
    }

    @GetMapping("/{petId}")
    public PetDTO getPet(@PathVariable long petId) {
        return convertPetToPetDto(this.petService.findPetById(petId));
    }

    @GetMapping
    public List<PetDTO> getPets() {
        return this.petService.findAllPets().stream().map(this::convertPetToPetDto).collect(toList());
    }

    @GetMapping("/owner/{ownerId}")
    public List<PetDTO> getPetsByOwner(@PathVariable long ownerId) {
        return this.petService.findAllPetsByOwnerId(ownerId).stream().map(this::convertPetToPetDto).collect(toList());
    }

    private Pet convertPetDtoToPet(PetDTO petDTO) {
        Pet pet = new Pet();
        copyProperties(petDTO, pet);
        if (petDTO.getOwnerId() != 0L) {
            pet.setOwner(this.customerService.findCustomerById(petDTO.getOwnerId()));
        }
        return pet;
    }

    private PetDTO convertPetToPetDto(Pet pet) {
        PetDTO petDTO = new PetDTO();
        copyProperties(pet, petDTO);
        petDTO.setOwnerId(ofNullable(pet.getOwner()).map(Customer::getId).orElse(0L));
        return petDTO;
    }

}