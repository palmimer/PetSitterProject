/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progmatic.petsitterproject.dtos;

import com.progmatic.petsitterproject.entities.PetType;
import com.progmatic.petsitterproject.entities.PlaceOfService;

/**
 *
 * @author progmatic
 */
public class SitterServiceDTO {
    private PlaceOfService place;
    private PetType petType;
    private int pricePerHour;
    private int pricePerDay;

    public SitterServiceDTO() {
    }

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
    
    
}
