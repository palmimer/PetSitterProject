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


public class UserRegistrationDTO {
    
    @NotNull
    @NotEmpty
    @Size(min = 4, message=("Legalább 4 karaktert írj!"))
    private String userName;
    @NotNull
    private String email;
    @NotNull
    @Size(min = 4, message=("Legalább 4 karaktert írj!"))
    private String password;

    public UserRegistrationDTO() {
    }
    
    
    public UserRegistrationDTO(String userName, String email, String password1) {
        this.userName = userName;
        this.password = password1;
        this.email = email;
    }

    public String getUserName() {
        return userName;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public void setUserName(String username) {
        this.userName = username;
    }

    

}
