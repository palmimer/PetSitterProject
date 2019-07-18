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
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

/**
 *
 * @author progmatic
 */

@Controller
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
    
    
    @GetMapping("petsitter/register")
    public String showRegisterPage(
        @ModelAttribute("registration") RegistrationDTO registration){
        return "register";
    }
    
    @PostMapping("petsitter/newregistration")
    public String register(
            @Valid
            @ModelAttribute("registration") RegistrationDTO registration, BindingResult bindingResult){
        
        if ( bindingResult.hasErrors() & !isRegistrationValid(registration, bindingResult)) {
            return "register";
        }

        try {
            
            userService.createUser(registration);
            
        } catch (AlreadyExistsException ex) {
            bindingResult.rejectValue("registration.email", "Már van ilyen felhasználó! Válasszon másik nevet!");
            return "register";
        }
        return "redirect:/petsitter/login";
    }
    
}
