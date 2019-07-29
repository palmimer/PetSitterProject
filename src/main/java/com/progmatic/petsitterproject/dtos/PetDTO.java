/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progmatic.petsitterproject.dtos;

import com.progmatic.petsitterproject.entities.PetType;

/**
 *
 * @author progmatic
 */
public class PetDTO {
     private String name;
     private PetType petType;
     private int id;

    public PetDTO() {
    }

    public PetDTO(String name, PetType petType) {
        this.name = name;
        this.petType = petType;
    }
    
    
    
    public PetDTO(String name, PetType petType, int petId){
        this.name = name;
        this.petType = petType;
        this.id = petId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PetType getPetType() {
        return petType;
    }

    public void setPetType(PetType petType) {
        this.petType = petType;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean equals(PetDTO otherPetDTO) {
        return this.name.equals(otherPetDTO.getName()) && this.petType.equals(otherPetDTO.getPetType());
    }
    
    
     
     
}
