package com.progmatic.petsitterproject.dtos;

import com.progmatic.petsitterproject.entities.Pet;
import java.util.List;
import javax.validation.constraints.NotBlank;

public class OwnerDTO {

    private List<Pet> pets;

    public OwnerDTO(List<Pet> pets) {
        this.pets = pets;
    }

    public void setPets(List<Pet> pets) {
        this.pets = pets;
    }
}
