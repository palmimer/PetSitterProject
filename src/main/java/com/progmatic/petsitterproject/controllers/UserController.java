/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progmatic.petsitterproject.controllers;

import com.progmatic.petsitterproject.dtos.ProfileEditDTO;
import com.progmatic.petsitterproject.dtos.RatingIncomingDTO;
import com.progmatic.petsitterproject.dtos.RatingResponseDTO;
import com.progmatic.petsitterproject.dtos.RegistrationDTO;
import com.progmatic.petsitterproject.dtos.SearchCriteriaDTO;
import com.progmatic.petsitterproject.dtos.SitterViewDTO;
import com.progmatic.petsitterproject.entities.ImageModel;
import com.progmatic.petsitterproject.entities.PetType;
import com.progmatic.petsitterproject.entities.Sitter;
import com.progmatic.petsitterproject.entities.User;
import com.progmatic.petsitterproject.entities.PlaceOfService;
import com.progmatic.petsitterproject.exceptions.NoSuchUserException;
import com.progmatic.petsitterproject.services.DTOConversion;
import com.progmatic.petsitterproject.services.EmailService;
import com.progmatic.petsitterproject.services.UserService;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.imageio.ImageIO;
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
    public Map<String, Object> registerNewUser(@RequestBody RegistrationDTO registration) throws AlreadyExistsException {

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
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Sikeres regisztráció! A belépéshez kérjük aktiváld fiókodat a címedre érkező üzenettel!");
        return response;
    }

    @GetMapping(value = "/sitters/search")
    public List<SitterViewDTO> listSitters(
            @RequestParam(value = "name", defaultValue="") String sitterName,
            @RequestParam(value = "place", required = false) PlaceOfService placeOfService,
            @RequestParam(value = "petType", required = false) PetType petType,
            @RequestParam(value = "postalCode", defaultValue="0") int postCode
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

    @PostMapping(value = "/user/{userId}/image")
    public Map<String, Object> uploadImage(@PathVariable("userId") int userId, @RequestParam("image") MultipartFile image) throws IOException, NoSuchUserException {

        //creates a cropped BufferedImage from the byte[]
        BufferedImage bufferedImage = cropImage(image.getBytes());
        byte[] imageInByte = convertsBufferedImageToByteArray(bufferedImage);
        //creates the ImageModel
        try {
            ImageModel pic = new ImageModel(userId, image.getName(), image.getContentType(), imageInByte);
            us.saveUserImage(userId, pic);
            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("message", "Image upload successful!");

            return responseMap;
        }catch(Exception e){
            throw new NoSuchUserException("The user whith this id does not exists!!!)");
        }
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

    @PostMapping(value = "/sitter/rating")
    public RatingResponseDTO rateSitter(@RequestBody RatingIncomingDTO newRating) throws AlreadyExistsException {
        us.addSitterRating(newRating);
        return us.sendBackAverageRating(newRating.getUserId());
    }

    public BufferedImage cropImage(byte[] image) throws IOException {

        final int targetSize = 500;
        InputStream in = new ByteArrayInputStream(image);
        BufferedImage originalImage = ImageIO.read(in);
        int height = originalImage.getHeight();
        int width = originalImage.getWidth();

        int newWidth;
        int newHeight;
        int cropStartPosX = 0;
        int cropStartPosY = 0;

        if (width > height) {   //landscape rectangle
            newWidth = (int) (targetSize / (double) height * width);
            newHeight = targetSize;
            cropStartPosX = newWidth / 2 - targetSize / 2;
            cropStartPosY = 0;

        } else if (width == height) {   //squere
            newWidth = targetSize;
            newHeight = targetSize;

        } else {   //portrait rectangle
            newWidth = targetSize;
            newHeight = (int) (targetSize / (double) width * height);
            cropStartPosX = 0;
            cropStartPosY = newHeight / 2 - targetSize / 2;
        }

        Image scaledInstance = originalImage.getScaledInstance(newWidth, newHeight, Image.SCALE_DEFAULT);
        BufferedImage scaledImage = new BufferedImage(newWidth, newHeight, BufferedImage.SCALE_DEFAULT);
        Graphics2D g2d = scaledImage.createGraphics();
        g2d.drawImage(scaledInstance, 0, 0, null);
        g2d.dispose();
        BufferedImage croppedImage = scaledImage.getSubimage(cropStartPosX, cropStartPosY, targetSize, targetSize);
        return croppedImage;
    }

    public byte[] convertsBufferedImageToByteArray(BufferedImage bufferedImage) throws IOException {

        byte[] imageInByte;
        try (
                ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            ImageIO.write(bufferedImage, "jpg", baos);
            baos.flush();
            imageInByte = baos.toByteArray();
        }
        return imageInByte;
    }

    
}
