/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progmatic.petsitterproject.dtos;

/**
 *
 * @author progmatic
 */
public class RegistrationDTO {
    private UserRegistrationDTO userData;
    private SitterRegistrationDTO sitterData;
    private OwnerDTO ownerData;

    public RegistrationDTO() {
    }
    

    public UserRegistrationDTO getUserData() {
        return userData;
    }

    public void setUserData(UserRegistrationDTO userData) {
        this.userData = userData;
    }

    public SitterRegistrationDTO getSitterData() {
        return sitterData;
    }

    public void setSitterData(SitterRegistrationDTO sitterData) {
        this.sitterData = sitterData;
    }

    public OwnerDTO getOwnerData() {
        return ownerData;
    }

    public void setOwnerData(OwnerDTO ownerData) {
        this.ownerData = ownerData;
    }
    
    
}
