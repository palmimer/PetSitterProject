package com.progmatic.petsitterproject.entities;

import java.io.Serializable;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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
    private ImageModel profilePhoto;
    @OneToOne
    private Address address;
    private String intro;
    @OneToMany(mappedBy = "sitter", fetch = FetchType.EAGER)
    private Set<SitterService> services;
    @OneToMany(mappedBy = "sitter", fetch = FetchType.EAGER)
    private Set<WorkingDay> availabilities;
    @OneToOne
    private User user;

    public Sitter() {
    }

    public Sitter(/*Byte[] profilePhoto,*/ String intro, User user) {
        //this.profilePhoto = profilePhoto;
        this.intro = intro;
        this.user = user;
    }

    public int getId() {
        return id;
    }

    public ImageModel getProfilePhoto() {
        return profilePhoto;
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
    
    public List<PlaceOfService> getPlacesOfService(){
        return services.stream().map(s -> s.getPlace()).distinct().collect(Collectors.toList()); 
    }

    public Set<WorkingDay> getAvailabilities() {
        return availabilities;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setProfilePhoto(ImageModel profilePhoto) {
        this.profilePhoto = profilePhoto;
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
    
}
