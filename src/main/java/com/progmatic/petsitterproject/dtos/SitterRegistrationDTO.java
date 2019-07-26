package com.progmatic.petsitterproject.dtos;

import com.progmatic.petsitterproject.entities.ImageModel;
import java.util.Set;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class SitterRegistrationDTO {
    @NotNull
    private String city;
    @NotNull
    private String address;
    @Min(1)
    private int postalCode;
    @NotNull
    private String intro;
    @NotNull
    private Set<SitterServiceDTO> services;
//    private PlaceOfService place;
//    @NotNull
//    private PetType petType;
//    private int pricePerHour;
//    private int pricePerDay;

    public SitterRegistrationDTO() {
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

//    public List<PetType> getPetTypes() {
//        List<PetType> petTypes = new ArrayList<>();
//        petTypes.add(petType);
//        return petTypes;
//    }

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

    public Set<SitterServiceDTO> getServices() {
        return services;
    }

    public void setServices(Set<SitterServiceDTO> services) {
        this.services = services;
    }
    
}
