package com.udacity.jdnd.course3.critter.entity;

import org.hibernate.annotations.Nationalized;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "customer")
public class Customer {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "ID", nullable = false, unique = true)
    private Long id;

    @Nationalized
    @Column(name = "name", nullable = false, length = 127)
    private String name;

    @Column(name = "PHONE_NO", length = 31)
    private String phoneNumber;

    @Column(name = "NOTES", length = 1023)
    private String notes;

    @OneToMany(mappedBy = "customer", cascade = ALL, orphanRemoval = true)
    private List<Pet> pets;

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

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getNotes() {
        return this.notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public List<Pet> getPets() {
        return this.pets;
    }

    public void setPets(List<Pet> pets) {
        this.pets = pets;
    }

}