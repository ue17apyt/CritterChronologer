package com.udacity.jdnd.course3.critter;

import com.udacity.jdnd.course3.critter.controller.PetController;
import com.udacity.jdnd.course3.critter.controller.ScheduleController;
import com.udacity.jdnd.course3.critter.controller.UserController;
import com.udacity.jdnd.course3.critter.dto.CustomerDTO;
import com.udacity.jdnd.course3.critter.dto.EmployeeDTO;
import com.udacity.jdnd.course3.critter.dto.EmployeeRequestDTO;
import com.udacity.jdnd.course3.critter.dto.PetDTO;
import com.udacity.jdnd.course3.critter.dto.ScheduleDTO;
import com.udacity.jdnd.course3.critter.enumeration.EmployeeSkill;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.IntStream;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Sets.newHashSet;
import static com.udacity.jdnd.course3.critter.enumeration.EmployeeSkill.FEEDING;
import static com.udacity.jdnd.course3.critter.enumeration.EmployeeSkill.PETTING;
import static com.udacity.jdnd.course3.critter.enumeration.EmployeeSkill.SHAVING;
import static com.udacity.jdnd.course3.critter.enumeration.EmployeeSkill.WALKING;
import static com.udacity.jdnd.course3.critter.enumeration.PetType.CAT;
import static com.udacity.jdnd.course3.critter.enumeration.PetType.DOG;
import static java.time.DayOfWeek.FRIDAY;
import static java.time.DayOfWeek.MONDAY;
import static java.time.DayOfWeek.SATURDAY;
import static java.time.DayOfWeek.SUNDAY;
import static java.time.DayOfWeek.THURSDAY;
import static java.time.DayOfWeek.TUESDAY;
import static java.time.DayOfWeek.WEDNESDAY;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * This is a set of functional tests to validate the basic capabilities desired for this application.
 * Students will need to configure the application to run these tests by adding application.properties file
 * to the test/resources directory that specifies the datasource. It can run using an in-memory H2 instance
 * and should not try to re-use the same datasource used by the rest of the app.
 * <p>
 * These tests should all pass once the project is complete.
 */
@Transactional
@SpringBootTest(classes = CritterApplication.class)
public class CritterFunctionalTest {

    Logger logger = Logger.getLogger(CritterFunctionalTest.class.getCanonicalName());

    @Autowired
    private UserController userController;

    @Autowired
    private PetController petController;

    @Autowired
    private ScheduleController scheduleController;

    private static EmployeeDTO createEmployeeDTO() {
        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setName("TestEmployee");
        employeeDTO.setSkills(newHashSet(FEEDING, PETTING));
        return employeeDTO;
    }

    private static PetDTO createPetDTO() {
        PetDTO petDTO = new PetDTO();
        petDTO.setName("TestPet");
        petDTO.setType(CAT);
        return petDTO;
    }

    private static EmployeeRequestDTO createEmployeeRequestDTO() {
        EmployeeRequestDTO employeeRequestDTO = new EmployeeRequestDTO();
        employeeRequestDTO.setDate(LocalDate.of(2019, 12, 25));
        employeeRequestDTO.setSkills(newHashSet(FEEDING, WALKING));
        return employeeRequestDTO;
    }

    private static void compareSchedules(ScheduleDTO scheduleDTO1, ScheduleDTO scheduleDTO2) {
        assertEquals(scheduleDTO1.getPetIds(), scheduleDTO2.getPetIds());
        assertEquals(scheduleDTO1.getActivities(), scheduleDTO2.getActivities());
        assertEquals(scheduleDTO1.getEmployeeIds(), scheduleDTO2.getEmployeeIds());
        assertEquals(scheduleDTO1.getDate(), scheduleDTO2.getDate());
    }

    @Test
    public void testCreateCustomer() {
        CustomerDTO customerDTO = createCustomerDTO();
        CustomerDTO newCustomer = this.userController.saveCustomer(customerDTO);
        CustomerDTO retrievedCustomer = this.userController.getAllCustomers().get(0);
        assertEquals(newCustomer.getName(), customerDTO.getName());
        assertEquals(newCustomer.getId(), retrievedCustomer.getId());
        assertTrue(retrievedCustomer.getId() > 0);
    }

    @Test
    public void testCreateEmployee() {
        EmployeeDTO employeeDTO = createEmployeeDTO();
        EmployeeDTO newEmployee = this.userController.saveEmployee(employeeDTO);
        EmployeeDTO retrievedEmployee = this.userController.getEmployee(newEmployee.getId());
        assertEquals(employeeDTO.getSkills(), newEmployee.getSkills());
        assertEquals(newEmployee.getId(), retrievedEmployee.getId());
        assertTrue(retrievedEmployee.getId() > 0);
    }

    @Test
    public void testAddPetsToCustomer() {

        CustomerDTO customerDTO = createCustomerDTO();
        CustomerDTO newCustomer = this.userController.saveCustomer(customerDTO);

        PetDTO petDTO = createPetDTO();
        petDTO.setOwnerId(newCustomer.getId());
        PetDTO newPet = this.petController.savePet(petDTO);

        // Make sure pet contains customer id
        PetDTO retrievedPet = this.petController.getPet(newPet.getId());
        assertEquals(retrievedPet.getId(), newPet.getId());
        assertEquals(retrievedPet.getOwnerId(), newCustomer.getId());

        // Make sure you can retrieve pets by owner
        List<PetDTO> pets = this.petController.getPetsByOwner(newCustomer.getId());
        assertEquals(newPet.getId(), pets.get(0).getId());
        assertEquals(newPet.getName(), pets.get(0).getName());

        // Check to make sure customer now also contains pet
        CustomerDTO retrievedCustomer = this.userController.getAllCustomers().get(0);
        assertTrue(retrievedCustomer.getPetIds() != null && retrievedCustomer.getPetIds().size() > 0);
        assertEquals(retrievedCustomer.getPetIds().get(0), retrievedPet.getId());

    }

    @Test
    public void testFindPetsByOwner() {

        CustomerDTO customerDTO = createCustomerDTO();
        CustomerDTO newCustomer = this.userController.saveCustomer(customerDTO);

        PetDTO petDTO = createPetDTO();
        petDTO.setOwnerId(newCustomer.getId());
        PetDTO newPet = this.petController.savePet(petDTO);
        petDTO.setType(DOG);
        petDTO.setName("DogName");
        PetDTO newPet2 = this.petController.savePet(petDTO);

        List<PetDTO> pets = this.petController.getPetsByOwner(newCustomer.getId());
        assertEquals(pets.size(), 2);
        assertEquals(pets.get(0).getOwnerId(), newCustomer.getId());
        assertEquals(pets.get(0).getId(), newPet.getId());

    }

    @Test
    public void testFindOwnerByPet() {

        CustomerDTO customerDTO = createCustomerDTO();
        CustomerDTO newCustomer = this.userController.saveCustomer(customerDTO);

        PetDTO petDTO = createPetDTO();
        petDTO.setOwnerId(newCustomer.getId());
        PetDTO newPet = this.petController.savePet(petDTO);

        CustomerDTO owner = this.userController.getOwnerByPet(newPet.getId());
        assertEquals(owner.getId(), newCustomer.getId());
        assertEquals(owner.getPetIds().get(0), newPet.getId());

    }

    @Test
    public void testChangeEmployeeAvailability() {

        EmployeeDTO employeeDTO = createEmployeeDTO();
        EmployeeDTO employeeDTO1 = this.userController.saveEmployee(employeeDTO);
        assertNull(employeeDTO1.getDaysAvailable());

        Set<DayOfWeek> availability = newHashSet(MONDAY, TUESDAY, WEDNESDAY);
        this.userController.setAvailability(availability, employeeDTO1.getId());

        EmployeeDTO employDTO2 = this.userController.getEmployee(employeeDTO1.getId());
        assertEquals(availability, employDTO2.getDaysAvailable());

    }

    private static CustomerDTO createCustomerDTO() {
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setName("TestEmployee");
        customerDTO.setPhoneNumber("123-456-789");
        return customerDTO;
    }

    @Test
    public void testFindEmployeesByServiceAndTime() {

        EmployeeDTO employeeDTO1 = createEmployeeDTO();
        EmployeeDTO employeeDTO2 = createEmployeeDTO();
        EmployeeDTO employeeDTO3 = createEmployeeDTO();

        employeeDTO1.setDaysAvailable(newHashSet(MONDAY, TUESDAY, WEDNESDAY));
        employeeDTO2.setDaysAvailable(newHashSet(WEDNESDAY, THURSDAY, FRIDAY));
        employeeDTO3.setDaysAvailable(newHashSet(FRIDAY, SATURDAY, SUNDAY));

        employeeDTO1.setSkills(newHashSet(FEEDING, PETTING));
        employeeDTO2.setSkills(newHashSet(PETTING, WALKING));
        employeeDTO3.setSkills(newHashSet(WALKING, SHAVING));

        EmployeeDTO savedEmployeeDTO1 = this.userController.saveEmployee(employeeDTO1);
        EmployeeDTO savedEmployeeDTO2 = this.userController.saveEmployee(employeeDTO2);
        EmployeeDTO savedEmployeeDTO3 = this.userController.saveEmployee(employeeDTO3);

        // Make a request that matches employee 1 or 2
        EmployeeRequestDTO employeeRequestDTO1 = new EmployeeRequestDTO();
        employeeRequestDTO1.setDate(LocalDate.of(2019, 12, 25)); // Wednesday
        employeeRequestDTO1.setSkills(newHashSet(PETTING));

        Set<Long> employeeIds1 = this.userController.findEmployeesForService(employeeRequestDTO1)
                .stream()
                .map(EmployeeDTO::getId)
                .collect(toSet());
        Set<Long> expectedEmployeeIds1 = newHashSet(savedEmployeeDTO1.getId(), savedEmployeeDTO2.getId());
        assertEquals(employeeIds1, expectedEmployeeIds1);

        // Make a request that matches only employee 3
        EmployeeRequestDTO employeeRequestDTO2 = new EmployeeRequestDTO();
        employeeRequestDTO2.setDate(LocalDate.of(2019, 12, 27)); // Friday
        employeeRequestDTO2.setSkills(newHashSet(WALKING, SHAVING));

        Set<Long> employeeIds2 = this.userController.findEmployeesForService(employeeRequestDTO2)
                .stream()
                .map(EmployeeDTO::getId)
                .collect(toSet());
        Set<Long> expectedEmployeeIds2 = newHashSet(savedEmployeeDTO3.getId());
        assertEquals(employeeIds2, expectedEmployeeIds2);

    }

    @Test
    public void testSchedulePetsForServiceWithEmployee() {

        EmployeeDTO tempEmployeeDTO = createEmployeeDTO();
        tempEmployeeDTO.setDaysAvailable(newHashSet(MONDAY, TUESDAY, WEDNESDAY));
        EmployeeDTO employeeDTO = this.userController.saveEmployee(tempEmployeeDTO);
        CustomerDTO customerDTO = this.userController.saveCustomer(createCustomerDTO());
        PetDTO tempPetDTO = createPetDTO();
        tempPetDTO.setOwnerId(customerDTO.getId());
        PetDTO petDTO = this.petController.savePet(tempPetDTO);

        LocalDate date = LocalDate.of(2019, 12, 25);
        List<Long> petList = newArrayList(petDTO.getId());
        List<Long> employeeList = newArrayList(employeeDTO.getId());
        Set<EmployeeSkill> skillSet = newHashSet(PETTING);

        this.scheduleController.createSchedule(createScheduleDTO(petList, employeeList, date, skillSet));
        ScheduleDTO scheduleDTO = this.scheduleController.getAllSchedules().get(0);

        assertEquals(scheduleDTO.getActivities(), skillSet);
        assertEquals(scheduleDTO.getDate(), date);
        assertEquals(scheduleDTO.getEmployeeIds(), employeeList);
        assertEquals(scheduleDTO.getPetIds(), petList);

    }

    private static ScheduleDTO createScheduleDTO(List<Long> petIds, List<Long> employeeIds, LocalDate date, Set<EmployeeSkill> activities) {
        ScheduleDTO scheduleDTO = new ScheduleDTO();
        scheduleDTO.setPetIds(petIds);
        scheduleDTO.setEmployeeIds(employeeIds);
        scheduleDTO.setDate(date);
        scheduleDTO.setActivities(activities);
        return scheduleDTO;
    }

    @Test
    public void testFindScheduleByEntities() {

        ScheduleDTO scheduleDTO1 = populateSchedule(
                1, 2, LocalDate.of(2019, 12, 25), newHashSet(FEEDING, WALKING)
        );
        ScheduleDTO scheduleDTO2 = populateSchedule(
                3, 1, LocalDate.of(2019, 12, 26), newHashSet(PETTING)
        );

        // Add a third schedule that shares some employees and pets with the other schedules
        ScheduleDTO scheduleDTO3 = new ScheduleDTO();
        scheduleDTO3.setEmployeeIds(scheduleDTO1.getEmployeeIds());
        scheduleDTO3.setPetIds(scheduleDTO2.getPetIds());
        scheduleDTO3.setActivities(newHashSet(SHAVING, PETTING));
        scheduleDTO3.setDate(LocalDate.of(2020, 3, 23));
        this.scheduleController.createSchedule(scheduleDTO3);

        /*
            We now have 3 schedule entries. The third schedule entry has the same employees as the 1st schedule
            and the same pets/owners as the second schedule. So if we look up schedule entries for the employee from
            schedule 1, we should get both the first and third schedule as our result.
         */

        // Employee 1 in is both schedule 1 and 3
        List<ScheduleDTO> scheduleDTOsForEmployee1 =
                this.scheduleController.getScheduleForEmployee(scheduleDTO1.getEmployeeIds().get(0));
        compareSchedules(scheduleDTO1, scheduleDTOsForEmployee1.get(0));
        compareSchedules(scheduleDTO3, scheduleDTOsForEmployee1.get(1));

        // Employee 2 is only in schedule 2
        List<ScheduleDTO> scheduleDTOsForEmployee2 =
                this.scheduleController.getScheduleForEmployee(scheduleDTO2.getEmployeeIds().get(0));
        compareSchedules(scheduleDTO2, scheduleDTOsForEmployee2.get(0));

        // Pet 1 is only in schedule 1
        List<ScheduleDTO> scheduleDTOsForPet1 =
                this.scheduleController.getScheduleForPet(scheduleDTO1.getPetIds().get(0));
        compareSchedules(scheduleDTO1, scheduleDTOsForPet1.get(0));

        // Pet from schedule 2 is in both schedules 2 and 3
        List<ScheduleDTO> scheduleDTOsForPet2 =
                this.scheduleController.getScheduleForPet(scheduleDTO2.getPetIds().get(0));
        compareSchedules(scheduleDTO2, scheduleDTOsForPet2.get(0));
        compareSchedules(scheduleDTO3, scheduleDTOsForPet2.get(1));

        // Owner of the first pet will only be in schedule 1
        List<ScheduleDTO> scheduleDTOsForCustomer1 = this.scheduleController.getScheduleForCustomer(
                this.userController.getOwnerByPet(scheduleDTO1.getPetIds().get(0)).getId()
        );
        compareSchedules(scheduleDTO1, scheduleDTOsForCustomer1.get(0));

        // Owner of pet from schedule 2 will be in both schedules 2 and 3
        List<ScheduleDTO> scheduleDTOsForCustomer2 = this.scheduleController.getScheduleForCustomer(
                this.userController.getOwnerByPet(scheduleDTO2.getPetIds().get(0)).getId()
        );
        compareSchedules(scheduleDTO2, scheduleDTOsForCustomer2.get(0));
        compareSchedules(scheduleDTO3, scheduleDTOsForCustomer2.get(1));

    }

    private ScheduleDTO populateSchedule(int numEmployees, int numPets, LocalDate date, Set<EmployeeSkill> activities) {
        List<Long> employeeIds = IntStream.range(0, numEmployees)
                .mapToObj(i -> createEmployeeDTO())
                .map(employeeDTO -> {
                    employeeDTO.setSkills(activities);
                    employeeDTO.setDaysAvailable(newHashSet(date.getDayOfWeek()));
                    return this.userController.saveEmployee(employeeDTO).getId();
                }).collect(toList());
        CustomerDTO customerDTO = this.userController.saveCustomer(createCustomerDTO());
        List<Long> petIds = IntStream.range(0, numPets)
                .mapToObj(i -> createPetDTO())
                .map(petDTO -> {
                    petDTO.setOwnerId(customerDTO.getId());
                    return this.petController.savePet(petDTO).getId();
                }).collect(toList());
        return this.scheduleController.createSchedule(createScheduleDTO(petIds, employeeIds, date, activities));
    }

}