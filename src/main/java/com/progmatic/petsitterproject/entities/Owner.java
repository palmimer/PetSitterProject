package com.progmatic.petsitterproject.entities;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Owner implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @OneToMany(mappedBy = "pet")
    private List<Pet> pets;

    public Owner() {
    }

    public Owner(List<Pet> pets) {
        this.pets = pets;
    }

    public int getId() {
        return id;
    }

    public List<Pet> getPets() {
        return pets;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setPets(Pet p) {
        pets.add(p);
    }
}
