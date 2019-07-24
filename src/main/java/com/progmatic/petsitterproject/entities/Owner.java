package com.progmatic.petsitterproject.entities;

import com.progmatic.petsitterproject.dtos.PetDTO;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import static javax.persistence.CascadeType.REMOVE;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

@Entity
public class Owner implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @OneToMany(cascade = REMOVE, mappedBy = "owner", fetch = FetchType.EAGER)
//    private List<Pet> pets;
    private Set<Pet> pets;
    @OneToOne
    private User user;

    public Owner() {
    }

    public Owner(Set<Pet> pets) {
        this.pets = pets;
    }

    public int getId() {
        return id;
    }

    public Set<Pet> getPets() {
        return pets;
    }
    
    public void setId(int id) {
        this.id = id;
    }

    public void setPets(Pet p) {
        pets.add(p);
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
    
    public Set<PetDTO> getPetDTOs(){
        Set<PetDTO> petDTOs = new HashSet<>();
        for (Pet pet : pets) {
            petDTOs.add(pet.getPetDTO());
        }
        return petDTOs;
    }
    
}
