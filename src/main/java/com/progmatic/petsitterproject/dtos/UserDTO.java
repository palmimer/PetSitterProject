package com.progmatic.petsitterproject.dtos;

import com.progmatic.petsitterproject.entities.User;
import javax.validation.constraints.NotNull;

public class UserDTO {

    @NotNull
    private int userId;
    @NotNull
    private String userName;
    @NotNull
    private String email;
    private OwnerDTO ownerData;
    private SitterResponseDTO sitterData;

    public UserDTO(User user) {
        this.userId = user.getId();
        this.userName = user.getName();
        this.email = user.getEmail();
        // mi van, ha null?
//        this.ownerData = new OwnerDTO(user.getOwner());
//        this.sitterData = new SitterViewDTO();
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return userName;
    }

    public String getEmail() {
        return email;
    }

    public void setUsername(String username) {
        this.userName = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public OwnerDTO getOwnerData() {
        return ownerData;
    }

    public void setOwnerData(OwnerDTO ownerData) {
        this.ownerData = ownerData;
    }

    public SitterResponseDTO getSitterData() {
        return sitterData;
    }

    public void setSitterData(SitterResponseDTO sitterData) {
        this.sitterData = sitterData;
    }
}
