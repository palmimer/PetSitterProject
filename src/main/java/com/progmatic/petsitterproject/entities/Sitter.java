package com.progmatic.petsitterproject.entities;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
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
    private Byte[] profilePhoto;
    @OneToOne
    private Address address;
    private String intro;
    
    @ElementCollection(targetClass = PetType.class)
    @Enumerated
    private List<PetType> petTypes;
    @OneToMany(mappedBy = "sitter")
    private List<SitterService> services;
    @OneToMany(mappedBy = "sitter")
    private List<WorkingDay> availabilities;

    public Sitter() {
    }

    public Sitter(Byte[] profilePhoto, Address address, String intro, List<PetType> petTypes, List<SitterService> services, List<WorkingDay> availabilities) {
        this.profilePhoto = profilePhoto;
        this.address = address;
        this.intro = intro;
        this.petTypes = petTypes;
        this.services = services;
        this.availabilities = availabilities;
    }

    public int getId() {
        return id;
    }

    public Byte[] getProfilePhoto() {
        return profilePhoto;
    }

    public Address getAddress() {
        return address;
    }

    public String getIntro() {
        return intro;
    }

    public List<PetType> getPetTypes() {
        return petTypes;
    }

    public List<SitterService> getServices() {
        return services;
    }

    public List<WorkingDay> getAvailabilities() {
        return availabilities;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setProfilePhoto(Byte[] profilePhoto) {
        this.profilePhoto = profilePhoto;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public void setPetTypes(List<PetType> petTypes) {
        this.petTypes = petTypes;
    }

    public void setServices(List<SitterService> services) {
        this.services = services;
    }

    public void setAvailabilities(List<WorkingDay> availabilities) {
        this.availabilities = availabilities;
    }
}
