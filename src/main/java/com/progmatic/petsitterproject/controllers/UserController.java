/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progmatic.petsitterproject.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.progmatic.petsitterproject.dtos.ProfileEditDTO;
import com.progmatic.petsitterproject.dtos.SearchCriteriaDTO;
import com.progmatic.petsitterproject.dtos.SitterViewDTO;
import com.progmatic.petsitterproject.entities.ImageModel;
import com.progmatic.petsitterproject.entities.PetType;
import com.progmatic.petsitterproject.entities.Sitter;
import com.progmatic.petsitterproject.entities.User;
import com.progmatic.petsitterproject.entities.PlaceOfService;
import com.progmatic.petsitterproject.services.DTOConversion;
import com.progmatic.petsitterproject.services.EmailService;
import com.progmatic.petsitterproject.services.FillerService;
import com.progmatic.petsitterproject.services.UserService;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    private FillerService fillerService;
    private EmailService emailService;
    
    @Autowired
    public UserController(UserService userService, FillerService fillerService, EmailService emailService) {
        this.us = userService;
        this.fillerService = fillerService;
        this.emailService = emailService;
    }

    @GetMapping(value = "sitter/{userId}")
    public SitterViewDTO singleSitter(@PathVariable("userId") int userId) {
        User user = us.getUser(userId);
        Sitter sitter = user.getSitter();
        SitterViewDTO response = DTOConversion.convertToSitterViewDTO(user, sitter);
        return response;
    }

    @GetMapping(value = "/sitters/search")
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
