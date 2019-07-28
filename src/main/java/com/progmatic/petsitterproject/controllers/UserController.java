/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progmatic.petsitterproject.controllers;

import com.progmatic.petsitterproject.dtos.PetDTO;
import com.progmatic.petsitterproject.dtos.ProfileEditDTO;
import com.progmatic.petsitterproject.dtos.RegistrationDTO;
import com.progmatic.petsitterproject.dtos.SearchCriteriaDTO;
import com.progmatic.petsitterproject.dtos.SitterRegistrationDTO;
import com.progmatic.petsitterproject.dtos.SitterViewDTO;
import com.progmatic.petsitterproject.entities.ImageModel;
import com.progmatic.petsitterproject.dtos.UserDTO;
import com.progmatic.petsitterproject.entities.PetType;
import com.progmatic.petsitterproject.entities.Sitter;
import com.progmatic.petsitterproject.entities.User;
import com.progmatic.petsitterproject.entities.PlaceOfService;
import com.progmatic.petsitterproject.services.DTOConversion;
import com.progmatic.petsitterproject.services.EmailService;
import com.progmatic.petsitterproject.services.UserService;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import javax.mail.MessagingException;
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

    @GetMapping(value = "/{userId}")
    public SitterViewDTO singleSitter(@PathVariable("userId") int userId) {
        User user = us.getUser(userId);
        Sitter sitter = user.getSitter();
        SitterViewDTO response = DTOConversion.convertToSitterViewDTO(user, sitter);
        return response;
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
     

    @GetMapping(value = "/search/sitters")
    public List<SitterViewDTO> listSitters(
            @RequestParam(value = "name", defaultValue = "") String sitterName,
            @RequestParam(value = "placeOfService", required = false) PlaceOfService placeOfService,
            @RequestParam(value = "petType", required = false) PetType petType,
            @RequestParam(value = "postCode", defaultValue = "0") int postCode
    ) {
        SearchCriteriaDTO criteria = new SearchCriteriaDTO(sitterName, postCode, placeOfService, petType);
        List<SitterViewDTO> selectedSitters = us.filterSitters(criteria);
        return selectedSitters;
    }

    @PostMapping("/editprofile")
    public String editProfile(@RequestBody ProfileEditDTO edit) {
        us.editProfile(edit);
        return "A profil módosult!";
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
