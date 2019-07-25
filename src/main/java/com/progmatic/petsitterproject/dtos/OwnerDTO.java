package com.progmatic.petsitterproject.dtos;

import com.progmatic.petsitterproject.entities.Owner;
import java.util.Set;

public class OwnerDTO {

    private Set<PetDTO> pets;

    public OwnerDTO() {
    }
   
    public OwnerDTO(Set<PetDTO> pets) {
        this.pets = pets;
    }
    
    public OwnerDTO(Owner owner){
        this.pets = owner.getPetDTOs();
    }

    public Set<PetDTO> getPets() {
        return pets;
    }

    public void setPets(Set<PetDTO> pets) {
        this.pets = pets;
    }
}
