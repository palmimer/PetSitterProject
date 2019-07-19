package com.progmatic.petsitterproject.dtos;

import com.progmatic.petsitterproject.entities.Address;
import com.progmatic.petsitterproject.entities.PetType;
import com.progmatic.petsitterproject.entities.SitterService;
import com.progmatic.petsitterproject.entities.WorkingDay;
import java.util.List;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class SitterRegistrationDTO {
    private Byte[] profilePhoto;
    @NotNull
    private String city;
    @NotNull
    private String address;
    @Min(1)
    private int postalCode;
    @NotNull
    private String intro;
    @NotNull
    private List<PetType> petTypes;
    @NotNull
    private List<SitterServiceDTO> services;
    private List<WorkingDay> availabilities;

    public SitterRegistrationDTO(Byte[] profilePhoto, String city, String address, int postalCode, String intro, List<PetType> petTypes, List<SitterServiceDTO> services, List<WorkingDay> availabilities) {
        this.profilePhoto = profilePhoto;
        this.city = city;
        this.address = address;
        this.postalCode = postalCode;
        this.intro = intro;
        this.petTypes = petTypes;
        this.services = services;
        this.availabilities = availabilities;
    }

    public Byte[] getProfilePhoto() {
        return profilePhoto;
    }

    public String getCity() {
        return city;
    }

    public int getPostalCode() {
        return postalCode;
    }
    
    public String getAddress() {
        return address;
    }

    public String getIntro() {
        return intro;
    }

    public List<PetType> getPetTypes() {
        return petTypes;
    }

    public List<SitterServiceDTO> getServices() {
        return services;
    }

    public List<WorkingDay> getAvailabilities() {
        return availabilities;
    }

    public void setProfilePhoto(Byte[] profilePhoto) {
        this.profilePhoto = profilePhoto;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setPostalCode(int postalCode) {
        this.postalCode = postalCode;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public void setPetTypes(List<PetType> petTypes) {
        this.petTypes = petTypes;
    }

    public void setServices(List<SitterServiceDTO> services) {
        this.services = services;
    }

    public void setAvailabilities(List<WorkingDay> availabilities) {
        this.availabilities = availabilities;
    }
}
