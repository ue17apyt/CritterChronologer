package com.udacity.jdnd.course3.critter.controller;

import com.udacity.jdnd.course3.critter.dto.ScheduleDTO;
import com.udacity.jdnd.course3.critter.entity.Employee;
import com.udacity.jdnd.course3.critter.entity.Pet;
import com.udacity.jdnd.course3.critter.entity.Schedule;
import com.udacity.jdnd.course3.critter.service.EmployeeService;
import com.udacity.jdnd.course3.critter.service.PetService;
import com.udacity.jdnd.course3.critter.service.ScheduleService;
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
 * Handles web requests related to Schedules.
 */
@RestController
@RequestMapping("/schedule")
public class ScheduleController {

    @Autowired
    private PetService petService;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private ScheduleService scheduleService;

    @PostMapping
    public ScheduleDTO createSchedule(@RequestBody ScheduleDTO scheduleDTO) {
        return convertScheduleToScheduleDto(
                this.scheduleService.saveSchedule(convertScheduleDtoToSchedule(scheduleDTO))
        );
    }

    @GetMapping
    public List<ScheduleDTO> getAllSchedules() {
        return this.scheduleService.findAllSchedules()
                .stream()
                .map(this::convertScheduleToScheduleDto)
                .collect(toList());
    }

    @GetMapping("/pet/{petId}")
    public List<ScheduleDTO> getScheduleForPet(@PathVariable long petId) {
        return ofNullable(this.scheduleService.findAllSchedulesByPetId(petId))
                .orElse(emptyList())
                .stream()
                .map(this::convertScheduleToScheduleDto)
                .collect(toList());
    }

    @GetMapping("/employee/{employeeId}")
    public List<ScheduleDTO> getScheduleForEmployee(@PathVariable long employeeId) {
        return ofNullable(this.scheduleService.findAllSchedulesByEmployeeId(employeeId))
                .orElse(emptyList())
                .stream()
                .map(this::convertScheduleToScheduleDto)
                .collect(toList());
    }

    @GetMapping("/customer/{customerId}")
    public List<ScheduleDTO> getScheduleForCustomer(@PathVariable long customerId) {
        return ofNullable(this.scheduleService.findAllSchedulesByCustomerId(customerId))
                .orElse(emptyList())
                .stream()
                .map(this::convertScheduleToScheduleDto)
                .collect(toList());
    }

    private Schedule convertScheduleDtoToSchedule(ScheduleDTO scheduleDTO) {
        Schedule schedule = new Schedule();
        copyProperties(scheduleDTO, schedule);
        List<Long> petIds = ofNullable(scheduleDTO.getPetIds()).orElse(emptyList());
        schedule.setPets(this.petService.findAllPetsByIds(petIds));
        List<Long> employeeIds = ofNullable(scheduleDTO.getEmployeeIds()).orElse(emptyList());
        schedule.setEmployees(this.employeeService.findAllEmployeesByIds(employeeIds));
        return schedule;
    }

    private ScheduleDTO convertScheduleToScheduleDto(Schedule schedule) {
        ScheduleDTO scheduleDTO = new ScheduleDTO();
        copyProperties(schedule, scheduleDTO);
        List<Long> petIds = ofNullable(schedule.getPets())
                .orElse(emptyList())
                .stream().map(Pet::getId)
                .collect(toList());
        scheduleDTO.setPetIds(petIds);
        List<Long> employeeIds = ofNullable(schedule.getEmployees())
                .orElse(emptyList())
                .stream()
                .map(Employee::getId)
                .collect(toList());
        scheduleDTO.setEmployeeIds(employeeIds);
        return scheduleDTO;
    }

}