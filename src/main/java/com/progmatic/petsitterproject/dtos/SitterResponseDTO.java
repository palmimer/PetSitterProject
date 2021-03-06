/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progmatic.petsitterproject.dtos;

import com.progmatic.petsitterproject.entities.Availability;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;

/**
 *
 * @author progmatic
 */
public class SitterResponseDTO {
    private String city;
    private String address;
    private int postalCode;
    private String intro;
    private Set<SitterServiceDTO> services;
    private List<WorkDayViewDTO> availabilities;

    public SitterResponseDTO() {
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
    
    
    
}
