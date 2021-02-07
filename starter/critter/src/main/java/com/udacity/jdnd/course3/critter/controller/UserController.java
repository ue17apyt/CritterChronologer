package com.udacity.jdnd.course3.critter.controller;

import com.udacity.jdnd.course3.critter.dto.CustomerDTO;
import com.udacity.jdnd.course3.critter.dto.EmployeeDTO;
import com.udacity.jdnd.course3.critter.dto.EmployeeRequestDTO;
import com.udacity.jdnd.course3.critter.entity.Customer;
import com.udacity.jdnd.course3.critter.entity.Employee;
import com.udacity.jdnd.course3.critter.entity.Pet;
import com.udacity.jdnd.course3.critter.exception.ObjectNotFoundException;
import com.udacity.jdnd.course3.critter.service.CustomerService;
import com.udacity.jdnd.course3.critter.service.EmployeeService;
import com.udacity.jdnd.course3.critter.service.PetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Set;

import static java.util.Collections.emptyList;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static org.springframework.beans.BeanUtils.copyProperties;

/**
 * Handles web requests related to Users.
 * <p>
 * Includes requests for both customers and employees. Splitting this into separate user and customer controllers
 * would be fine too, though that is not part of the required scope for this class.
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private PetService petService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private EmployeeService employeeService;

    @PostMapping("/customer")
    public CustomerDTO saveCustomer(@RequestBody CustomerDTO customerDTO) {
        Customer customer = this.customerService.saveCustomer(convertCustomerDtoToCustomer(customerDTO));
        customerDTO.setId(customer.getId());
        return customerDTO;
    }

    @GetMapping("/customer")
    public List<CustomerDTO> getAllCustomers() {
        return this.customerService.findAllCustomers().stream()
                .map(this::convertCustomerToCustomerDto)
                .collect(toList());
    }

    @GetMapping("/customer/pet/{petId}")
    public CustomerDTO getOwnerByPet(@PathVariable long petId) {
        return ofNullable(this.petService.findPetById(petId).getOwner())
                .map(this::convertCustomerToCustomerDto)
                .orElseThrow(() -> new ObjectNotFoundException("Owner does not exist"));
    }

    @PostMapping("/employee")
    public EmployeeDTO saveEmployee(@RequestBody EmployeeDTO employeeDTO) {
        Employee employee = this.employeeService.saveEmployee(convertEmployeeDtoToEmployee(employeeDTO));
        employeeDTO.setId(employee.getId());
        return employeeDTO;
    }

    @PostMapping("/employee/{employeeId}")
    public EmployeeDTO getEmployee(@PathVariable long employeeId) {
        return convertEmployeeToEmployeeDto(this.employeeService.findEmployeeById(employeeId));
    }

    @PutMapping("/employee/{employeeId}")
    public void setAvailability(@RequestBody Set<DayOfWeek> daysAvailable, @PathVariable long employeeId) {
        Employee employee = this.employeeService.findEmployeeById(employeeId);
        employee.setDaysAvailable(daysAvailable);
        this.employeeService.saveEmployee(employee);
    }

    @GetMapping("/employee/availability")
    public List<EmployeeDTO> findEmployeesForService(@RequestBody EmployeeRequestDTO employeeDTO) {
        List<Employee> employees = this.employeeService.findEmployeesBySkillsAndDaysAvailable(
                employeeDTO.getSkills(), employeeDTO.getDate().getDayOfWeek()
        );
        return employees.stream().map(this::convertEmployeeToEmployeeDto).collect(toList());
    }

    private Customer convertCustomerDtoToCustomer(CustomerDTO customerDTO) {
        Customer customer = new Customer();
        copyProperties(customerDTO, customer);
        List<Long> petIds = ofNullable(customerDTO.getPetIds()).orElse(emptyList());
        customer.setPets(this.petService.findAllPetsByIds(petIds));
        return customer;
    }

    private CustomerDTO convertCustomerToCustomerDto(Customer customer) {
        CustomerDTO customerDTO = new CustomerDTO();
        copyProperties(customer, customerDTO);
        List<Long> petIds = ofNullable(customer.getPets())
                .orElse(null)
                .stream()
                .map(Pet::getId)
                .collect(toList());
        customerDTO.setPetIds(petIds);
        return customerDTO;
    }

    private Employee convertEmployeeDtoToEmployee(EmployeeDTO employeeDTO) {
        Employee employee = new Employee();
        copyProperties(employeeDTO, employee);
        return employee;
    }

    private EmployeeDTO convertEmployeeToEmployeeDto(Employee employee) {
        EmployeeDTO employeeDTO = new EmployeeDTO();
        copyProperties(employee, employeeDTO);
        return employeeDTO;
    }

}