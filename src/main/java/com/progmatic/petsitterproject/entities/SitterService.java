package com.progmatic.petsitterproject.entities;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class SitterService implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private PlaceOfService place;
    private PetType petType;
    private int pricePerHour;
    private int pricePerDay;
    
    @ManyToOne
    private Sitter sitter;

    public SitterService() {
    }

    public SitterService(PlaceOfService place, PetType petType, int pricePerHour, int pricePerDay) {
        this.place = place;
        this.petType = petType;
        this.pricePerHour = pricePerHour;
        this.pricePerDay = pricePerDay;
    }

    public int getId() {
        return id;
    }

    public PlaceOfService getPlace() {
        return place;
    }

    public PetType getPetType() {
        return petType;
    }

    public int getPricePerHour() {
        return pricePerHour;
    }

    public int getPricePerDay() {
        return pricePerDay;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setPlace(PlaceOfService place) {
        this.place = place;
    }

    public void setPetType(PetType petType) {
        this.petType = petType;
    }

    public void setPricePerHour(int pricePerHour) {
        this.pricePerHour = pricePerHour;
    }

    public void setPricePerDay(int pricePerDay) {
        this.pricePerDay = pricePerDay;
    }
}
