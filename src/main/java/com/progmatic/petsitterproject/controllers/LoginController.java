/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progmatic.petsitterproject.controllers;

import com.progmatic.petsitterproject.dtos.RegistrationDTO;
import com.progmatic.petsitterproject.services.EmailService;
import com.progmatic.petsitterproject.services.FillerService;
import com.progmatic.petsitterproject.services.UserService;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author progmatic
 */

@RestController
public class LoginController {
    
    private UserService userService;
    private FillerService fillerService;
    private EmailService emailService;
    
    @Autowired
    public LoginController(UserService userService, FillerService fillerService
            , EmailService emailService) {
        
        this.userService = userService;
        this.fillerService = fillerService;
        this.emailService = emailService;
    }

    
    //milyen oldalt mutat, amikor beléptünk? (azt, amit eddig)
    
    @PostMapping("/newregistration")
    public String registerNewUser(
            @Valid
            @RequestBody RegistrationDTO registration) throws AlreadyExistsException{
    
        fillerService.fixDatabase();
        userService.createUser(registration);
        emailService.sendSimpleActivatorMessage(registration.getEmail());
        return "Sikeres regisztráció!";
    }
    
    @GetMapping("/verify")
    public String activateUser(@RequestParam("ver") String valid){
        emailService.activateUser(valid);
        return "Sikeres érvényesítés!";
    }
    
    @GetMapping("/suspendaccount")
    public String removeSelf(){
        userService.suspendAccount();
        return "Fiókját felfüggesztettük.";
    }
    
}
