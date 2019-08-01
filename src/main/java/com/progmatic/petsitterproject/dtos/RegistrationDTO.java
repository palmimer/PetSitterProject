/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progmatic.petsitterproject.dtos;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author progmatic
 */
public class RegistrationDTO {
    
    @NotNull
    @NotEmpty
    @Size(min = 4, message=("Legalább 4 karaktert írj!"))
    private String userName;
    @NotNull
    private String email;
    @NotNull
    @Size(min = 4, message=("Legalább 4 karaktert írj!"))
    private String password;
    private SitterRegistrationDTO sitterData;
    private OwnerDTO ownerData;

    public RegistrationDTO() {
    }
    

    public UserRegistrationDTO getUserData() {
        return new UserRegistrationDTO(userName, email, password);
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    
    
    
}
