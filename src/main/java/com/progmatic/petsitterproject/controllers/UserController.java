/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progmatic.petsitterproject.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.progmatic.petsitterproject.dtos.ProfileEditDTO;
import com.progmatic.petsitterproject.dtos.RegistrationDTO;
import com.progmatic.petsitterproject.dtos.SearchCriteriaDTO;
import com.progmatic.petsitterproject.dtos.SitterViewDTO;
import com.progmatic.petsitterproject.dtos.UserDTO;
import com.progmatic.petsitterproject.entities.ImageModel;
import com.progmatic.petsitterproject.entities.PetType;
import com.progmatic.petsitterproject.entities.Sitter;
import com.progmatic.petsitterproject.entities.User;
import com.progmatic.petsitterproject.entities.PlaceOfService;
import com.progmatic.petsitterproject.services.DTOConversion;
import com.progmatic.petsitterproject.services.EmailService;
import com.progmatic.petsitterproject.services.UserService;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author progmatic
 */
@RestController
public class UserController {

    private UserService us;
    private EmailService es;

    @Autowired
    public UserController(UserService us, EmailService es) {
        this.us = us;
        this.es = es;
    }

    @GetMapping(value = "sitter/{userId}")
    public SitterViewDTO singleSitter(@PathVariable("userId") int userId) {
        User user = us.getUser(userId);
        Sitter sitter = user.getSitter();
        SitterViewDTO response = DTOConversion.convertToSitterViewDTO(user, sitter);
        return response;
        //return us.getUserDTO();
    }
    
    @PostMapping("/newregistration")
    public String registerNewUser(@RequestBody RegistrationDTO registration) throws AlreadyExistsException{
        
        us.createUser(registration.getUserData());
        System.out.println("user regisztráció sikerült");
        if (registration.getOwnerData() != null) {
            us.registerNewOwner(registration.getUserData().getEmail(), registration.getOwnerData().getPets());
            System.out.println("owner regisztráció sikerült");
        }
        if (registration.getSitterData() != null) {
            us.registerNewSitter(registration.getUserData().getEmail(), registration.getSitterData());
            System.out.println("sitter regisztráció sikerült");
        }
        try {
            es.sendActivatorLink(registration.getUserData().getEmail());
        } catch (MessagingException e) {
            throw new AlreadyExistsException("Az érvényesítő üzenet elküldése "
                    + "sajnos meghiúsult! Kérj fiók-visszaállítást az érvényesítéshez!");
        }
        return "Sikeres regisztráció! A belépéshez kérjük aktiváld fiókodat a címedre érkező üzenettel!";
    }
     

    @GetMapping(value = "/sitters/search")
    public List<SitterViewDTO> listSitters(
            @RequestParam(value = "name", defaultValue="") String sitterName,
            @RequestParam(value = "placeOfService", defaultValue="") String placeOfService,
            @RequestParam(value = "petType", defaultValue="") String petType,
            @RequestParam(value = "postCode", defaultValue="") String postCode
    ) {
        SearchCriteriaDTO criteria = new SearchCriteriaDTO(sitterName, decipherPostcode(postCode), decipherPlace(placeOfService), decipherPetType(petType));
        List<SitterViewDTO> selectedSitters = us.filterSitters(criteria);
        return selectedSitters;
    }

    @PostMapping("/modifyprofile")
    public Map<String, Object> editProfile(@RequestBody ProfileEditDTO editedProfile) {
        us.editProfile(editedProfile);
        
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("message", "A profil módosult!");
        responseMap.put("user", us.getUserDTO());
        
        return responseMap;
    }

//    @PostMapping("/removepet")
//    public String removePet(@RequestBody PetDTO pet){
//        us.removePet(pet);
//        return "Az állatot eltávolítottuk a nyilvántartásból.";
//    }
//    @GetMapping(value = "/sitter/image/{sitterId}")
//    public ResponseEntity<byte[]> testImage(@PathVariable("sitterId") int sitterId){
////        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(us.getUser(sitterId).getSitter().getProfilePhoto().getPic());
//        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(us.image(1).getPic());
//    }
    @PostMapping(value = "/user/{userId}/image")
    public String uploadImage(@PathVariable("userId") int userId, @RequestParam("image") MultipartFile image) throws IOException {
        ImageModel pic = new ImageModel(userId, image.getName(), image.getContentType(), image.getBytes());
        us.saveUserImage(userId, pic);
        return "Image upload successful!";
    }

    @GetMapping(value = "/user/{userId}/image")
    public ResponseEntity<byte[]> showImage(@PathVariable("userId") int userId) {
        return ResponseEntity
                .ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(us
                        .getUser(userId)
                        .getProfilePhoto()
                        .getPic());
    }
    
    private PlaceOfService decipherPlace(String place){
        if(place.isEmpty()){
            return null;
        }
        return PlaceOfService.valueOf(place);
    }
    private PetType decipherPetType(String petType){
        if(petType.isEmpty()){
            return null;
        }
        return PetType.valueOf(petType);
    }
    
    private int decipherPostcode(String code){
        if(code.isEmpty()){
            return 0;
        }
        return Integer.parseInt(code);
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
