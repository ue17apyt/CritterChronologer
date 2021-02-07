package com.udacity.jdnd.course3.critter.entity;

import com.udacity.jdnd.course3.critter.enumeration.EmployeeSkill;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.EnumType.STRING;
import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

@Entity
public class Schedule {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private LocalDate date;

    @ManyToMany(targetEntity = Employee.class, fetch = LAZY, cascade = ALL)
    private List<Employee> employees;

    @ManyToMany(targetEntity = Pet.class, fetch = LAZY, cascade = ALL)
    private List<Pet> pets;

    @ElementCollection
    @Enumerated(STRING)
    private Set<EmployeeSkill> activities;

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return this.date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public List<Employee> getEmployees() {
        return this.employees;
    }

    public void setEmployees(List<Employee> employees) {
        this.employees = employees;
    }

    public List<Pet> getPets() {
        return this.pets;
    }

    public void setPets(List<Pet> pets) {
        this.pets = pets;
    }

    public Set<EmployeeSkill> getActivities() {
        return this.activities;
    }

    public void setActivities(Set<EmployeeSkill> activities) {
        this.activities = activities;
    }

}