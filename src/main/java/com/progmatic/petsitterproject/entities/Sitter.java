package com.progmatic.petsitterproject.entities;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;
import static javax.persistence.CascadeType.REMOVE;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

@Entity
public class Sitter implements Serializable {

    @Column(name = "SITTER_ID")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @OneToOne
    private Address address;
    private String intro;
    @OneToMany(cascade = REMOVE, mappedBy = "sitter", fetch = FetchType.EAGER)
    private Set<SitterService> services;
    @OneToMany(cascade = REMOVE, mappedBy = "sitter", fetch = FetchType.EAGER)
    private Set<WorkingDay> availabilities;
    @OneToMany(mappedBy = "sitter", fetch = FetchType.EAGER)
    private Set<SittingWork> petSittings;
    private int[] ratings = new int[0];
    
    @OneToOne
    private User user;

    public Sitter() {
    }

    public Sitter(String intro, User user) {
        this.intro = intro;
        this.user = user;
    }

    public int getId() {
        return id;
    }

    public Address getAddress() {
        return address;
    }

    public String getIntro() {
        return intro;
    }

    public List<PetType> getPetTypes() {
        return services.stream().map(s -> s.getPetType()).distinct().collect(Collectors.toList());
    }

    public Set<SitterService> getServices() {
        return services;
    }

    public List<PlaceOfService> getPlacesOfService() {
        return services.stream().map(s -> s.getPlace()).distinct().collect(Collectors.toList());
    }

    public Set<WorkingDay> getAvailabilities() {
        return availabilities;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

//    public void setPetTypes(List<PetType> petTypes) {
//        this.petTypes = petTypes;
//    }
    public void setServices(Set<SitterService> services) {
        this.services = services;
    }

    public void setAvailabilities(Set<WorkingDay> availabilities) {
        this.availabilities = availabilities;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<SittingWork> getPetSittings() {
        return petSittings;
    }

    public void setPetSittings(Set<SittingWork> works) {
        this.petSittings = works;
    }
    
    

    public int[] getRatings() {
        return ratings;
    }

    public void setRatings(int[] ratings) {
        this.ratings = ratings;
    }
    
    public void setRating(int rating){
        int[] newRatings = new int[this.ratings.length + 1];
        for (int i = 0; i < this.ratings.length; i++) {
            newRatings[i] = this.ratings[i];
        }
        newRatings[newRatings.length - 1] = rating;
        this.ratings = newRatings;
    }

    public double getAverageRating() {
        return calculateAverageRating();
    }
    
    public int getNumberOfRatings(){
        if (ratings.length != 0) {
            return ratings.length;
        } else {
            return 0;
        }
    }

    private double calculateAverageRating() {
        int sum = 0;
        if (this.ratings.length != 0) {
            for (int i = 0; i < this.ratings.length; i++) {
                sum += ratings[i];
            }
            return (double) sum / ratings.length;
        }
        return 0;
    }
    

}
