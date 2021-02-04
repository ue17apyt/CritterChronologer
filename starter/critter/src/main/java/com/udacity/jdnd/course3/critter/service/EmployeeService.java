package com.udacity.jdnd.course3.critter.service;

import com.udacity.jdnd.course3.critter.entity.Employee;
import com.udacity.jdnd.course3.critter.enumeration.EmployeeSkill;
import com.udacity.jdnd.course3.critter.exception.ObjectNotFoundException;
import com.udacity.jdnd.course3.critter.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.DayOfWeek;
import java.util.List;
import java.util.Set;

import static java.util.Collections.emptyList;
import static java.util.Objects.nonNull;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;

@Transactional
@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    public List<Employee> findAllEmployeesByIds(List<Long> employeeIds) {
        return ofNullable(
                this.employeeRepository.findAllById(ofNullable(employeeIds).orElse(emptyList()))
        ).orElse(emptyList());
    }

    public Employee findEmployeeById(Long employeeId) {
        return this.employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ObjectNotFoundException("Employee ID" + employeeId + "does not exist"));
    }

    public List<Employee> findEmployeesBySkillsAndDaysAvailable(Set<EmployeeSkill> skills, DayOfWeek dayOfWeek) {
        return ofNullable(this.employeeRepository.findByDaysAvailableContains(dayOfWeek))
                .orElse(emptyList())
                .stream()
                .filter(employee -> nonNull(employee))
                .filter(employee -> nonNull(employee.getSkills()))
                .filter(employee -> employee.getSkills().containsAll(skills))
                .collect(toList());
    }

    public Employee saveEmployee(Employee employee) {
        return this.employeeRepository.save(
                ofNullable(employee).orElseThrow(() -> new ObjectNotFoundException("Employee does not exist"))
        );
    }

}