/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progmatic.petsitterproject.controllers;

import com.progmatic.petsitterproject.dtos.ProfileEditDTO;
import com.progmatic.petsitterproject.dtos.UserDTO;
import com.progmatic.petsitterproject.services.EmailService;
import com.progmatic.petsitterproject.services.FillerService;
import com.progmatic.petsitterproject.services.UserService;
import javax.mail.MessagingException;
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
    public LoginController(UserService userService, FillerService fillerService, EmailService emailService) {
        
        this.userService = userService;
        this.fillerService = fillerService;
        this.emailService = emailService;
    }

    
    //milyen oldalt mutat, amikor beléptünk? (azt, amit eddig)
    
    
    @PostMapping("/checkuser")
    public UserDTO checkIfUserIsLoggedIn() throws NoUserLoggedInException{
        if (userService.getCurrentUser() == null) {
            throw new NoUserLoggedInException("Nincs bejelentkezett felhasználó!");
        } else {
            return userService.getUserDTO();
        }
    }
        
    @GetMapping("/verify")
    public String activateUser(@RequestParam("ver") String valid) throws AlreadyExistsException{
        emailService.activateUser(valid);
        return "Sikeres hitelesítés!";
    }
    
    @GetMapping("/suspendaccount")
    public String removeSelf(){
        userService.suspendAccount();
        return "Fiókodat felfüggesztettük. Ha meggondolod magad, 48 órán belül visszaállíthatod!";
    }
    
    @PostMapping("/restoreaccount")
    public String restoreSelf(@RequestBody ProfileEditDTO req) throws AlreadyExistsException, MessagingException{
        emailService.sendReactivator(req.getEmail());
        return "Fiókodat visszaállítottuk!";
    }
    
    @GetMapping("/filler")
    public String fillers(){
        fillerService.fixDatabase();
        return "Megtöltve!";
    }
    
    @PostMapping("/resetpassword")
    public String resetPassword(@RequestBody ProfileEditDTO req) throws AlreadyExistsException, MessagingException{
        emailService.passwordReset(req.getEmail());
        return "A pótjelszót elküldtük a megadott címre!";
    }
    
}
