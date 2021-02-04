package com.udacity.jdnd.course3.critter.entity;

import com.udacity.jdnd.course3.critter.enumeration.PetType;
import org.hibernate.annotations.Nationalized;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDate;

import static javax.persistence.EnumType.STRING;
import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "pet")
public class Pet {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "ID", nullable = false, unique = true)
    private Long id;

    @Nationalized
    @Column(name = "NAME", nullable = false, length = 63)
    private String name;

    @Enumerated(STRING)
    @Column(name = "TYPE")
    private PetType type;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "CUSTOMER_ID")
    private Customer owner;

    @Column(name = "BIRTH_DATE")
    private LocalDate birthDate;

    @Column(name = "NOTES", length = 1023)
    private String notes;

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

    public PetType getType() {
        return this.type;
    }

    public void setType(PetType type) {
        this.type = type;
    }

    public Customer getOwner() {
        return this.owner;
    }

    public void setOwner(Customer owner) {
        this.owner = owner;
    }

    public LocalDate getBirthDate() {
        return this.birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getNotes() {
        return this.notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

}