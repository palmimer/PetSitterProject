/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progmatic.petsitterproject.dtos;

import com.progmatic.petsitterproject.entities.Address;
import com.progmatic.petsitterproject.entities.PetType;
import com.progmatic.petsitterproject.entities.PlaceOfService;
import com.progmatic.petsitterproject.entities.SitterService;

/**
 *
 * @author progmatic
 */
public class SearchCriteriaDTO {
    private String name;
    private int postCode;
    private PlaceOfService placeOfService;
    private PetType petType;

    public SearchCriteriaDTO(String name, int postCode, PlaceOfService placeOfService, PetType petType) {
        this.name = name;
        this.postCode = postCode;
        this.placeOfService = placeOfService;
        this.petType = petType;
    }

    public int getPostCode() {
        return postCode;
    }

    public void setPostCode(int postCode) {
        this.postCode = postCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PlaceOfService getPlaceOfService() {
        return placeOfService;
    }

    public void setPlaceOfService(PlaceOfService placeOfService) {
        this.placeOfService = placeOfService;
    }

    public PetType getPetType() {
        return petType;
    }

    public void setPetType(PetType petType) {
        this.petType = petType;
    }

    
    
     
    
}
