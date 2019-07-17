package com.progmatic.petsitterproject.entities;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Service implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private PlaceOfService place;
    private TypeOfService type;
    private int pricePerHour;
    private int pricePerDay;

    public Service() {
    }

    public Service(int id, PlaceOfService place, TypeOfService type, int pricePerHour, int pricePerDay) {
        this.id = id;
        this.place = place;
        this.type = type;
        this.pricePerHour = pricePerHour;
        this.pricePerDay = pricePerDay;
    }

    public int getId() {
        return id;
    }

    public PlaceOfService getPlace() {
        return place;
    }

    public TypeOfService getType() {
        return type;
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

    public void setType(TypeOfService type) {
        this.type = type;
    }

    public void setPricePerHour(int pricePerHour) {
        this.pricePerHour = pricePerHour;
    }

    public void setPricePerDay(int pricePerDay) {
        this.pricePerDay = pricePerDay;
    }
}
