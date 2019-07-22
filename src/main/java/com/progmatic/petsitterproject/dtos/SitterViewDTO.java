/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progmatic.petsitterproject.dtos;

import com.progmatic.petsitterproject.entities.PetType;
import com.progmatic.petsitterproject.entities.SitterService;
import com.progmatic.petsitterproject.entities.WorkingDay;
import java.util.List;

/**
 *
 * @author progmatic
 */

public class SitterViewDTO {
    private Byte[] profilePhoto;
    private String UserName;
    private String city;
    private String address;
    private int postalCode;
    private String intro;
    private List<PetType> petTypes;
    private List<SitterService> services;
    private List<WorkingDay> availabilities;

    public SitterViewDTO() {
    }

    public Byte[] getProfilePhoto() {
        return profilePhoto;
    }

    public void setProfilePhoto(Byte[] profilePhoto) {
        this.profilePhoto = profilePhoto;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(int postalCode) {
        this.postalCode = postalCode;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public List<PetType> getPetTypes() {
        return petTypes;
    }

    public void setPetTypes(List<PetType> petTypes) {
        this.petTypes = petTypes;
    }

    public List<SitterService> getServices() {
        return services;
    }

    public void setServices(List<SitterService> services) {
        this.services = services;
    }

    public List<WorkingDay> getAvailabilities() {
        return availabilities;
    }

    public void setAvailabilities(List<WorkingDay> availabilities) {
        this.availabilities = availabilities;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String UserName) {
        this.UserName = UserName;
    }
    
    
}
