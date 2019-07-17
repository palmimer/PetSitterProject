package com.progmatic.petsitterproject.entities;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.MapKey;
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
    @OneToMany
    private List<PetType> petTypes;
    @OneToMany
    private List<Service> services;
    @OneToMany
    private List<WorkingDay> availabilities;

    public Sitter() {
    }

    public Sitter(int id, Byte[] profilePhoto, Address address, String intro, List<PetType> petTypes, List<Service> services, List<WorkingDay> availabilities) {
        this.id = id;
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

    public List<Service> getServices() {
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

    public void setServices(List<Service> services) {
        this.services = services;
    }

    public void setAvailabilities(List<WorkingDay> availabilities) {
        this.availabilities = availabilities;
    }
}
