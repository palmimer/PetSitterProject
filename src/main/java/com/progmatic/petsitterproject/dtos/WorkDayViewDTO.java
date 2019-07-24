/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progmatic.petsitterproject.dtos;

import com.progmatic.petsitterproject.entities.Availability;

/**
 *
 * @author imaginifer
 */
public class WorkDayViewDTO {
    int id;
    Availability availability;
    String date;

    public WorkDayViewDTO(int id, Availability availability, String date) {
        this.id = id;
        this.availability = availability;
        this.date = date;
    }

    public WorkDayViewDTO() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Availability getAvailability() {
        return availability;
    }

    public void setAvailability(Availability availability) {
        this.availability = availability;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
    
    
}


