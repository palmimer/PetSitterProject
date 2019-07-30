/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progmatic.petsitterproject.entities;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 *
 * @author imaginifer
 */
@Entity
public class SittingWork implements Serializable{
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne
    private Owner owner;
    @ManyToOne
    private Sitter sitter;
    private LocalDate dayOfWork;
    private LocalDateTime creationDate;
    private boolean agreedOn;

    public SittingWork(Owner owner, Sitter sitter, LocalDate dayOfWork, LocalDateTime creationDate) {
        this.owner = owner;
        this.sitter = sitter;
        this.dayOfWork = dayOfWork;
        this.creationDate = creationDate;
        this.agreedOn = false;
    }

    public SittingWork() {
    }

    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Owner getOwner() {
        return owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    public Sitter getSitter() {
        return sitter;
    }

    public void setSitter(Sitter sitter) {
        this.sitter = sitter;
    }

    public LocalDate getDayOfWork() {
        return dayOfWork;
    }

    public void setDayOfWork(LocalDate dayOfWork) {
        this.dayOfWork = dayOfWork;
    }

    public boolean isAgreedOn() {
        return agreedOn;
    }

    public void setAgreedOn(boolean agreedOn) {
        this.agreedOn = agreedOn;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }
    
    
    
    
}
