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
import com.progmatic.petsitterproject.entities.Service;
import com.progmatic.petsitterproject.services.UserService;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
    
    @GetMapping(value = "/petsitter/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public SitterDTO singleSitter( @PathVariable("userId") int userId ){
        User user = us.getUser(userId);
        Sitter sitter = user.getSitter();
        SitterDTO response = convertToDTO(user, sitter);
        return response;
    }
    
    @GetMapping(value = "/petsitter/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<SitterDTO> listSitters(
                @RequestParam(value = "serviceType", defaultValue = "all") String serviceType,
                @RequestParam(value = "petType", defaultValue = "dog") String petType,
                @RequestParam(value = "postCode", defaultValue = "") int postCode
                
    ){
//        Service service = createService(serviceType, petType);
//        SearchCriteriaDTO criteria = new SearchCriteriaDTO(postCode, service);
        List<SitterDTO> selectedSitters = new ArrayList<>();
//                us.filterSitters(criteria);
        return selectedSitters;
    }
    
    private SitterDTO convertToDTO(User user, Sitter sitter) {
        SitterDTO response = new SitterDTO(
                sitter.getProfilePhoto(),
                sitter.getAddress(),
                sitter.getIntro(),
                sitter.getPetTypes(),
                sitter.getServices(),
                sitter.getAvailabilities()
        );
        return response;
    }
    
//    private Service createService(String serviceType, String petType) {
//        Service service = new Service();
//        if (serviceType.equals("owners_home")) {
//            service.setPlace(PlaceOfService.OWNERS_HOME);
//        } else {
//            service.setPlace(PlaceOfService.SITTERS_HOME);
//        }
//        switch(petType){
//            case ("dog"):
//                service.setPetType(PetType.DOG);
//                return service;
//            case ("cat"):
//                service.setPetType(PetType.CAT);
//                return service;
//            case ("bird"):
//                service.setPetType(PetType.BIRD);
//                return service;
//            case ("rodent"):
//                service.setPetType(PetType.RODENT);
//                return service;
//            case ("reptile"):
//                service.setPetType(PetType.REPTILE);
//                return service;
//        }
//        
//    }
    
}
