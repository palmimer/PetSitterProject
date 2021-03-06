package com.progmatic.petsitterproject.entities;

import com.progmatic.petsitterproject.dtos.PetDTO;
import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Pet implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    @Enumerated(EnumType.STRING)
    private PetType petType;
    private String name;
    @ManyToOne
    private Owner owner;

    public Pet() {
    }

    public Pet(PetType petType, String name) {
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

    public Owner getOwner() {
        return owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }
    
    public PetDTO getPetDTO(){
        return new PetDTO(this.name, this.petType, this.id);
    }
}
