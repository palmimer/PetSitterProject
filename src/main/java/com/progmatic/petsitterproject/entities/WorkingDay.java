package com.progmatic.petsitterproject.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
public class WorkingDay implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private LocalDate wDay;
    private Availability availability;

    @ManyToOne
    private Sitter sitter;

    public Sitter getSitter() {
        return sitter;
    }

    public void setSitter(Sitter sitter) {
        this.sitter = sitter;
    }

    public WorkingDay() {
    }

    public WorkingDay(LocalDate wDay, Availability availability) {
        this.wDay = wDay;
        this.availability = availability;
    }

    public int getId() {
        return id;
    }

    public LocalDate getwDay() {
        return wDay;
    }

    public Availability getAvailability() {
        return availability;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setwDay(LocalDate wDay) {
        this.wDay = wDay;
    }

    public void setAvailability(Availability availability) {
        this.availability = availability;
    }
}
