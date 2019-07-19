/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progmatic.petsitterproject.controllers;

import com.progmatic.petsitterproject.dtos.SearchCriteriaDTO;
import com.progmatic.petsitterproject.dtos.SitterDTO;
import com.progmatic.petsitterproject.entities.Address;
import com.progmatic.petsitterproject.entities.PetType;
import com.progmatic.petsitterproject.entities.Sitter;
import com.progmatic.petsitterproject.entities.User;
import com.progmatic.petsitterproject.entities.PlaceOfService;
import com.progmatic.petsitterproject.entities.SitterService;
import com.progmatic.petsitterproject.services.UserService;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author progmatic
 */
@RestController
public class UserController {
    
    private UserService us;

    @Autowired
    public UserController(UserService us) {
        this.us = us;
    }
    
    @GetMapping(value = "/petsitter")
    public String showHomePage(){
        return "homepage";
    }
    
    @GetMapping(value = "/petsitter/becomeasitter")
    public String showSitterRegistrationForm(){
        return "sitterform";
    }
    
    @PostMapping(value = "/petsitter/newsitter")
    public void registerNewSitter(){
        
    }
    
    @GetMapping(value = "/petsitter/{userId}")
    public SitterDTO singleSitter( @PathVariable("userId") int userId ){
        User user = us.getUser(userId);
        Sitter sitter = user.getSitter();
        SitterDTO response = convertToDTO(user, sitter);
        return response;
    }
//    
    @GetMapping(value = "/petsitter/search")
    public List<SitterDTO> listSitters(
                @RequestParam(value = "name", defaultValue = "") String sitterName,
                @RequestParam(value = "PlaceOfService", defaultValue = "null") PlaceOfService placeOfService,
                @RequestParam(value = "petType", defaultValue = "null") PetType petType,
                @RequestParam(value = "postCode", defaultValue = "0") int postCode
                
    ){
        SearchCriteriaDTO criteria = new SearchCriteriaDTO(sitterName, postCode, placeOfService, petType);
        List<SitterDTO> selectedSitters =  us.filterSitters(criteria);
        return selectedSitters;
    }
    
    
    
    private SitterDTO convertToDTO(User user, Sitter sitter) {
        SitterDTO response = new SitterDTO(
                sitter.getProfilePhoto(),
                sitter.getAddress().getCity(),
                sitter.getAddress().getAddress(),
                sitter.getAddress().getPostalCode(),
                sitter.getIntro(),
                sitter.getPetTypes(),
                sitter.getServices(),
                sitter.getAvailabilities()
        );
        return response;
    }
    
//    
//        
//    }
    
}
