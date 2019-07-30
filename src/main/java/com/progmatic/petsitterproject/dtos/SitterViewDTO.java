/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progmatic.petsitterproject.dtos;

import java.util.List;
import java.util.Set;

/**
 *
 * @author progmatic
 */
public class SitterViewDTO {

    private String username;
    private String city;
    private String address;
    private int postalCode;
    private String intro;
    //private List<PetType> petTypes;
    private Set<SitterServiceDTO> services;
    private List<WorkDayViewDTO> availabilities;
    private double averageRating;
    private int numberOfRatings;
    private int id;

    public SitterViewDTO() {
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

    public Set<SitterServiceDTO> getServices() {
        return services;
    }

    public void setServices(Set<SitterServiceDTO> services) {
        this.services = services;
    }

    public List<WorkDayViewDTO> getAvailabilities() {
        return availabilities;
    }

    public void setAvailabilities(List<WorkDayViewDTO> availabilities) {
        this.availabilities = availabilities;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String userName) {
        this.username = userName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(double averageRating) {
        this.averageRating = averageRating;
    }

    public int getNumberOfRatings() {
        return numberOfRatings;
    }

    public void setNumberOfRatings(int numberOfRatings) {
        this.numberOfRatings = numberOfRatings;
    }
    
}
