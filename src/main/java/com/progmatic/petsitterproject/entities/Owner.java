package com.progmatic.petsitterproject.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import static javax.persistence.CascadeType.REMOVE;
import javax.persistence.Entity;
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
    @OneToMany(cascade = REMOVE, mappedBy = "owner")
    private List<Pet> pets = new ArrayList<>();
    
    @OneToOne(cascade = REMOVE)
    private User user;

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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
    
    
}
