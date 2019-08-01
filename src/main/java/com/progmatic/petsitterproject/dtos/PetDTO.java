/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progmatic.petsitterproject.dtos;

import com.progmatic.petsitterproject.entities.PetType;
import java.util.Objects;

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

//    public boolean equals(PetDTO otherPetDTO) {
//        return this.name.equals(otherPetDTO.getName()) && this.petType.equals(otherPetDTO.getPetType());
//    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 67 * hash + Objects.hashCode(this.name);
        hash = 67 * hash + Objects.hashCode(this.petType);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final PetDTO other = (PetDTO) obj;
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        if (this.petType != other.petType) {
            return false;
        }
        return true;
    }
    
    

    
    
     
     
}
