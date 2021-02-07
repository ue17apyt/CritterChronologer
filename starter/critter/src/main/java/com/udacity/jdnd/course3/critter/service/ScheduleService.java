package com.udacity.jdnd.course3.critter.service;

import com.udacity.jdnd.course3.critter.entity.Schedule;
import com.udacity.jdnd.course3.critter.repository.ScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Transactional
@Service
public class ScheduleService {

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private CustomerService customerService;

    public List<Schedule> findAllSchedules() {
        return this.scheduleRepository.findAll();
    }

    public List<Schedule> findAllSchedulesByPetId(Long petId) {
        return this.scheduleRepository.findByPetsId(petId);
    }

    public List<Schedule> findAllSchedulesByEmployeeId(Long employeeId) {
        return this.scheduleRepository.findByEmployeesId(employeeId);
    }

    public List<Schedule> findAllSchedulesByCustomerId(Long customerId) {
        return this.customerService.findCustomerById(customerId).getPets()
                .stream()
                .map(pet -> findAllSchedulesByPetId(pet.getId()))
                .flatMap(List::stream)
                .collect(toList());
    }

    public Schedule saveSchedule(Schedule schedule) {
        return this.scheduleRepository.save(schedule);
    }

}