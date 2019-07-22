/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progmatic.petsitterproject.controllers;

import com.progmatic.petsitterproject.dtos.PetDTO;
import com.progmatic.petsitterproject.dtos.SearchCriteriaDTO;
import com.progmatic.petsitterproject.dtos.SitterRegistrationDTO;
import com.progmatic.petsitterproject.dtos.SitterViewDTO;
import com.progmatic.petsitterproject.entities.PetType;
import com.progmatic.petsitterproject.entities.Sitter;
import com.progmatic.petsitterproject.entities.User;
import com.progmatic.petsitterproject.entities.PlaceOfService;
import com.progmatic.petsitterproject.services.DTOConversionService;
import com.progmatic.petsitterproject.services.UserService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author progmatic
 */
@RestController
public class UserController {
    
    private UserService us;
    private DTOConversionService cs;

    @Autowired
    public UserController(UserService us) {
        this.us = us;
    }
    
    @PostMapping(value = "/newsitter")
    public String registerNewSitter(@RequestBody SitterRegistrationDTO sitterData){
        
        us.registerNewSitter(sitterData);
        //valami visszajelzést arról, hogy megtörtént a regisztráció
        return "Sikeresen regisztráltál, mint KiVi!";
    }
    
    @PostMapping(value = "/newowner")
    public String registerNewOwner(@RequestBody PetDTO petData){
        
        us.registerNewOwner(petData.getPetType(), petData.getName());
        //valami visszajelzést arról, hogy megtörtént a regisztráció
        return "Sikeresen regisztráltál, mint állattulajdonos!";
    }
    
    @GetMapping(value = "/{userId}")
    public SitterViewDTO singleSitter( @PathVariable("userId") int userId ){
        User user = us.getUser(userId);
        Sitter sitter = user.getSitter();
        SitterViewDTO response = cs.convertToSitterViewDTO(user, sitter);
        return response;
    }
//    
    @GetMapping(value = "/search/sitters")
    public List<SitterViewDTO> listSitters(
                @RequestParam(value = "name", defaultValue = "") String sitterName,
                @RequestParam(value = "PlaceOfService", defaultValue = "null") PlaceOfService placeOfService,
                @RequestParam(value = "petType", defaultValue = "null") PetType petType,
                @RequestParam(value = "postCode", defaultValue = "0") int postCode
                
    ){
        SearchCriteriaDTO criteria = new SearchCriteriaDTO(sitterName, postCode, placeOfService, petType);
        List<SitterViewDTO> selectedSitters =  us.filterSitters(criteria);
        return selectedSitters;
    }
    
    
    
//    private SitterViewDTO convertToDTO(User user, Sitter sitter) {
//        SitterViewDTO response = new SitterViewDTO();
//        response.setProfilePhoto(sitter.getProfilePhoto());
//        response.setUserName(user.getName());
//        response.setCity(sitter.getAddress().getCity());
//        response.setAddress(sitter.getAddress().getAddress());
//        response.setPostalCode(sitter.getAddress().getPostalCode());
//        response.setIntro(sitter.getIntro());
//        response.setPetTypes(sitter.getPetTypes());
//        response.setServices(sitter.getServices());
//        response.setAvailabilities(sitter.getAvailabilities());
//        
//        return response;
//    }
    
//    
//        
//    }
    
}
