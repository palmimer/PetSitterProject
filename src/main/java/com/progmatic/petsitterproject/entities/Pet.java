package com.progmatic.petsitterproject.entities;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Pet implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private PetType petType;
    private String name;

    public Pet() {
    }

    public Pet(int id, PetType petType, String name) {
        this.id = id;
        this.petType = petType;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public PetType getPetType() {
        return petType;
    }

    public String getName() {
        return name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setPetType(PetType petType) {
        this.petType = petType;
    }

    public void setName(String name) {
        this.name = name;
    }
}
