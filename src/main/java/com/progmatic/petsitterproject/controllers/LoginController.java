/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progmatic.petsitterproject.controllers;

import com.progmatic.petsitterproject.dtos.RegistrationDTO;
import com.progmatic.petsitterproject.services.UserService;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author progmatic
 */

@RestController
public class LoginController {
    
    private UserService userService;

    @Autowired
    public LoginController(UserService userService) {
        this.userService = userService;
    }
    
    @GetMapping("petsitter/signin")
    public String showLoginPage(){
        return "login";
    }
    
    //milyen oldalt mutat, amikor beléptünk? (azt, amit eddig)
    
    
    @GetMapping("petsitter/register")
    public String showRegisterPage(
        @ModelAttribute("registration") RegistrationDTO registration){
        return "register";
    }
    
    @PostMapping("/petsitter/newregistration")
    public void register(
            @Valid
            @RequestBody RegistrationDTO registration) throws AlreadyExistsException{
        
  
            
            userService.createUser(registration);
         
    }
    
}
