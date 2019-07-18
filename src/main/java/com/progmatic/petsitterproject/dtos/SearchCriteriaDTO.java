/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progmatic.petsitterproject.dtos;

import com.progmatic.petsitterproject.entities.Address;
import com.progmatic.petsitterproject.entities.PetType;
import com.progmatic.petsitterproject.entities.SitterService;

/**
 *
 * @author progmatic
 */
public class SearchCriteriaDTO {
    
    private int postCode;
    private SitterService service;

    public SearchCriteriaDTO(int postCode, SitterService service) {
        this.postCode = postCode;
        this.service = service;
    }

    public int getPostCode() {
        return postCode;
    }

    public void setPostCode(int postCode) {
        this.postCode = postCode;
    }

    public SitterService getService() {
        return service;
    }

    public void setService(SitterService service) {
        this.service = service;
    }

    
     
    
}
