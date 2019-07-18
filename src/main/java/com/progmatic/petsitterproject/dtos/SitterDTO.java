package com.progmatic.petsitterproject.dtos;

import com.progmatic.petsitterproject.entities.Address;
import com.progmatic.petsitterproject.entities.PetType;
import com.progmatic.petsitterproject.entities.Service;
import com.progmatic.petsitterproject.entities.WorkingDay;
import java.util.List;
import javax.validation.constraints.NotNull;

public class SitterDTO {
    private Byte[] profilePhoto;
    @NotNull
    private Address address;
    @NotNull
    private String intro;
    @NotNull
    private List<PetType> petTypes;
    @NotNull
    private List<Service> services;
    private List<WorkingDay> availabilities;

    public SitterDTO(Byte[] profilePhoto, Address address, String intro, List<PetType> petTypes, List<Service> services, List<WorkingDay> availabilities) {
        this.profilePhoto = profilePhoto;
        this.address = address;
        this.intro = intro;
        this.petTypes = petTypes;
        this.services = services;
        this.availabilities = availabilities;
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
