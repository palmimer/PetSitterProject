/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progmatic.petsitterproject.dtos;

import com.progmatic.petsitterproject.entities.Availability;
import com.progmatic.petsitterproject.entities.ImageModel;
import com.progmatic.petsitterproject.entities.PetType;
import com.progmatic.petsitterproject.entities.SitterService;
import com.progmatic.petsitterproject.entities.WorkingDay;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;
import javax.validation.constraints.NotNull;

/**
 *
 * @author progmatic
 */

public class SitterViewDTO {
    private ImageModel profilePhoto;
    private String UserName;
    private String city;
    private String address;
    private int postalCode;
    private String intro;
    //private List<PetType> petTypes;
    private Set<SitterServiceDTO> services;
    private List<WorkDayViewDTO> availabilities;
    private int id;

    public SitterViewDTO() {
    }

    public ImageModel getProfilePhoto() {
        return profilePhoto;
    }

    public void setProfilePhoto(ImageModel profilePhoto) {
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

//    public List<PetType> getPetTypes() {
//        return petTypes;
//    }
//
//    public void setPetTypes(List<PetType> petTypes) {
//        this.petTypes = petTypes;
//    }

    public Set<SitterServiceDTO> getServices() {
        return services;
    }

    public void setServices(Set<SitterServiceDTO> services) {
        this.services = services;
    }

    public TreeMap<LocalDate, Availability> getAvailabilities() {
        return availabilities;
    }

    public void setAvailabilities(TreeMap<LocalDate, Availability> availabilities) {
        this.availabilities = availabilities;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String UserName) {
        this.UserName = UserName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    
    
    
}
