/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progmatic.petsitterproject.dtos;

import com.progmatic.petsitterproject.entities.Address;
import com.progmatic.petsitterproject.entities.PetType;
import com.progmatic.petsitterproject.entities.Service;

/**
 *
 * @author progmatic
 */
public class SearchCriteriaDTO {
    
    private int postCode;
    private Service service;

    public SearchCriteriaDTO(int postCode, Service service) {
        this.postCode = postCode;
        this.service = service;
    }

    public int getPostCode() {
        return postCode;
    }

    public void setPostCode(int postCode) {
        this.postCode = postCode;
    }

    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
    }

    
     
    
}
