/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progmatic.petsitterproject.controllers;

import com.progmatic.petsitterproject.dtos.PetDTO;
import com.progmatic.petsitterproject.dtos.RegistrationDTO;
import com.progmatic.petsitterproject.dtos.SearchCriteriaDTO;
import com.progmatic.petsitterproject.dtos.SitterRegistrationDTO;
import com.progmatic.petsitterproject.dtos.SitterViewDTO;
import com.progmatic.petsitterproject.entities.PetType;
import com.progmatic.petsitterproject.entities.Sitter;
import com.progmatic.petsitterproject.entities.User;
import com.progmatic.petsitterproject.entities.PlaceOfService;
import com.progmatic.petsitterproject.services.DTOConversion;
import com.progmatic.petsitterproject.services.UserService;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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
    
    @PostMapping("/newregistration")
    public String registerNewUser(@RequestBody RegistrationDTO registration) throws AlreadyExistsException{
    
        us.fixDatabase();
        int newUserID = us.createUser(registration.getUserData());
        System.out.println("user regisztráció sikerült");
        if (registration.getOwnerData() != null) {
            us.registerNewOwner(newUserID, registration.getOwnerData().getPets());
            System.out.println("owner regisztráció sikerült");
        }
        if (registration.getSitterData() != null) {
            us.registerNewSitter(newUserID, registration.getSitterData());
            System.out.println("sitter regisztráció sikerült");
        }
        return "Sikeres regisztráció!";
    }
    
//    @PostMapping(value = "/newsitter")
//    public String registerNewSitter(@RequestBody SitterRegistrationDTO sitterData){
//        
//        us.registerNewSitter(sitterData);
//        //valami visszajelzést arról, hogy megtörtént a regisztráció
//        return "Sikeresen regisztráltál, mint KiVi!";
//    }
//    
//    @PostMapping(value = "/newowner")
//    public String registerNewOwner(@RequestBody Set<PetDTO> petData){
//        
//        us.registerNewOwner(petData);
//        
//        // visszajelzés arról, hogy megtörtént a regisztráció
//        return "Sikeresen regisztráltál, mint állattulajdonos!";
//    }
    
    @RequestMapping(value = "/owner", method = RequestMethod.PUT)
    public String addNewPetToOwner(@RequestBody Set<PetDTO> petData){
        int userId = us.getUserIdOfCurrentUser();
        us.registerNewOwner(userId, petData);
        return "Sikeresen hozzáadtad újabb állatodat!";
    }
    
    @GetMapping(value = "/{userId}")
    public SitterViewDTO singleSitter( @PathVariable("userId") int userId ){
        User user = us.getUser(userId);
        Sitter sitter = user.getSitter();
        SitterViewDTO response = DTOConversion.convertToSitterViewDTO(user, sitter);
        return response;
    }
    
    @GetMapping(value = "/search/sitters")
    public List<SitterViewDTO> listSitters(
                @RequestParam(value = "name", defaultValue = "") String sitterName,
                @RequestParam(value = "placeOfService", required = false) PlaceOfService placeOfService,
                @RequestParam(value = "petType", required = false) PetType petType,
                @RequestParam(value = "postCode", defaultValue = "0") int postCode
                
    ){
        SearchCriteriaDTO criteria = new SearchCriteriaDTO(sitterName, postCode, placeOfService, petType);
        List<SitterViewDTO> selectedSitters =  us.filterSitters(criteria);
        return selectedSitters;
    }
    
//    @GetMapping(value = "/sitter/image/{sitterId}")
//    public ResponseEntity<byte[]> testImage(@PathVariable("sitterId") int sitterId){
////        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(us.getUser(sitterId).getSitter().getProfilePhoto().getPic());
//        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(us.image(1).getPic());
//    }
    
    
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
