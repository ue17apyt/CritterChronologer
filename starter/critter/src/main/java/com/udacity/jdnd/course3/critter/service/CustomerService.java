package com.udacity.jdnd.course3.critter.service;

import com.udacity.jdnd.course3.critter.entity.Customer;
import com.udacity.jdnd.course3.critter.exception.ObjectNotFoundException;
import com.udacity.jdnd.course3.critter.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    public List<Customer> findAllCustomers() {
        return this.customerRepository.findAll();
    }

    public Customer findCustomerById(Long customerId) {
        return this.customerRepository.findById(customerId)
                .orElseThrow(() -> new ObjectNotFoundException("Customer ID" + customerId + "does not exist"));
    }

    public Customer saveCustomer(Customer customer) {
        return this.customerRepository.save(customer);
    }

}