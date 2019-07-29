/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progmatic.petsitterproject.dtos;

import com.progmatic.petsitterproject.entities.ImageModel;

/**
 *
 * @author imaginifer
 */
public class ProfileEditDTO {
    private String username, password;
    private SitterViewDTO sitterData;
    private OwnerDTO ownerData;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
    
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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
    
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    
    
    
}
