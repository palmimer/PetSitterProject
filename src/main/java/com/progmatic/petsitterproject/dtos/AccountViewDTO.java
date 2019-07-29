/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progmatic.petsitterproject.dtos;

/**
 *
 * @author imaginifer
 */
public class AccountViewDTO {
    private int id;
    private String name, email;
    private boolean isActivated;
    private SitterViewDTO sitterData;
    private OwnerDTO ownerData;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public SitterViewDTO getSitterData() {
        return sitterData;
    }

    public void setSitterData(SitterViewDTO sitterData) {
        this.sitterData = sitterData;
    }

    public OwnerDTO getOwnerData() {
        return ownerData;
    }

    public void setOwnerData(OwnerDTO ownerData) {
        this.ownerData = ownerData;
    }

    public boolean isIsActivated() {
        return isActivated;
    }

    public void setIsActivated(boolean isActivated) {
        this.isActivated = isActivated;
    }
    
    
    
    
}
