package com.progmatic.petsitterproject.dtos;

import com.progmatic.petsitterproject.entities.PetType;
import com.progmatic.petsitterproject.entities.PlaceOfService;
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
//    private List<PetType> petTypes;
//    @NotNull
    private PlaceOfService place;
    @NotNull
    private PetType petType;
    private int pricePerHour;
    private int pricePerDay;
    
    private List<WorkingDay> availabilities;

    public SitterRegistrationDTO() {
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

//    public List<PetType> getPetTypes() {
//        return petTypes;
//    }

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

//    public void setPetTypes(List<PetType> petTypes) {
//        this.petTypes = petTypes;
//    }

    public PlaceOfService getPlace() {
        return place;
    }

    public void setPlace(PlaceOfService place) {
        this.place = place;
    }

    public PetType getPetType() {
        return petType;
    }

    public void setPetType(PetType petType) {
        this.petType = petType;
    }

    public int getPricePerHour() {
        return pricePerHour;
    }

    public void setPricePerHour(int pricePerHour) {
        this.pricePerHour = pricePerHour;
    }

    public int getPricePerDay() {
        return pricePerDay;
    }

    public void setPricePerDay(int pricePerDay) {
        this.pricePerDay = pricePerDay;
    }

    

    public void setAvailabilities(List<WorkingDay> availabilities) {
        this.availabilities = availabilities;
    }
}