package com.udacity.jdnd.course3.critter.service;

import com.udacity.jdnd.course3.critter.entity.Schedule;
import com.udacity.jdnd.course3.critter.exception.ObjectNotFoundException;
import com.udacity.jdnd.course3.critter.repository.ScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

import static java.util.Collections.emptyList;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;

@Transactional
@Service
public class ScheduleService {

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private CustomerService customerService;

    public List<Schedule> findAllSchedules() {
        return ofNullable(this.scheduleRepository.findAll()).orElse(emptyList());
    }

    public List<Schedule> findAllSchedulesByPetId(Long petId) {
        return ofNullable(this.scheduleRepository.findByPetId(petId)).orElse(emptyList());
    }

    public List<Schedule> findAllSchedulesByEmployeeId(Long employeeId) {
        return ofNullable(this.scheduleRepository.findByEmployeeId(employeeId)).orElse(emptyList());
    }

    public List<Schedule> findAllSchedulesByCustomerId(Long customerId) {
        return ofNullable(this.customerService.findCustomerById(customerId).getPets())
                .orElse(emptyList())
                .stream()
                .map(pet -> findAllSchedulesByPetId(pet.getId()))
                .flatMap(List::stream)
                .collect(toList());
    }

    public Schedule saveSchedule(Schedule schedule) {
        return this.scheduleRepository.save(
                ofNullable(schedule).orElseThrow(() -> new ObjectNotFoundException("Schedule does not exist"))
        );
    }

}