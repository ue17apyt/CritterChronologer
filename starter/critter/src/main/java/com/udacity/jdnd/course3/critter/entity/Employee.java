package com.udacity.jdnd.course3.critter.entity;

import com.udacity.jdnd.course3.critter.enumeration.EmployeeSkill;
import org.hibernate.annotations.Nationalized;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.DayOfWeek;
import java.util.Set;

import static javax.persistence.EnumType.STRING;
import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "employee")
public class Employee {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "ID", nullable = false, unique = true)
    private Long id;

    @Nationalized
    @Column(name = "NAME", nullable = false, length = 127)
    private String name;

    @ElementCollection
    @Enumerated(STRING)
    @Column(name = "SKILLS")
    private Set<EmployeeSkill> skills;

    @ElementCollection
    @Enumerated(STRING)
    @Column(name = "DAYS_AVAILABLE")
    private Set<DayOfWeek> daysAvailable;

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<EmployeeSkill> getSkills() {
        return this.skills;
    }

    public void setSkills(Set<EmployeeSkill> skills) {
        this.skills = skills;
    }

    public Set<DayOfWeek> getDaysAvailable() {
        return this.daysAvailable;
    }

    public void setDaysAvailable(Set<DayOfWeek> daysAvailable) {
        this.daysAvailable = daysAvailable;
    }

}