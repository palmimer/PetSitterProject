/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progmatic.petsitterproject.controllers;

import com.progmatic.petsitterproject.dtos.PetDTO;
import com.progmatic.petsitterproject.dtos.ResetDTO;
import com.progmatic.petsitterproject.dtos.UserDTO;
import com.progmatic.petsitterproject.services.AdminService;
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
    
    private UserService us;
    private FillerService fs;
    private EmailService es;
    private AdminService as;
    
    @Autowired
    public LoginController(UserService userService, FillerService fillerService
            , EmailService emailService, AdminService adminService) {
        
        this.us = userService;
        this.fs = fillerService;
        this.es = emailService;
        this.as = adminService;
    }

    
    //milyen oldalt mutat, amikor beléptünk? (azt, amit eddig)
    
    
    @PostMapping("/checkuser")
    public UserDTO checkIfUserIsLoggedIn() throws NoUserLoggedInException{
        if (us.getCurrentUser() == null) {
            throw new NoUserLoggedInException("Nincs bejelentkezett felhasználó!");
        } else {
            return us.getUserDTO();
        }
    }
        
    @GetMapping("/verify")
    public String activateUser(@RequestParam("ver") String valid) throws AlreadyExistsException{
        es.activateUser(valid);
        return "Sikeres hitelesítés!";
    }
    
    @GetMapping("/suspendaccount")
    public String removeSelf(){
        us.suspendAccount();
        return "Fiókodat felfüggesztettük. Ha meggondolod magad, 48 órán belül visszaállíthatod!";
    }
    
    @PostMapping("/restoreaccount")
    public String restoreSelf(@RequestBody ResetDTO req) throws AlreadyExistsException, MessagingException{
        es.sendReactivator(req.getEmail());
        return "Fiókodat visszaállítottuk!";
    }
    
    @GetMapping("/filler")
    public String fillers(){
        fs.fixDatabase();
        return "Megtöltve!";
    }
    
    @PostMapping("/resetpassword")
    public String resetPassword(@RequestBody ResetDTO req) throws AlreadyExistsException, MessagingException{
        es.passwordReset(req.getEmail());
        return "A pótjelszót elküldtük a megadott címre!";
    }
    
    @PostMapping("/suspenduser")
    public String suspendUser(@RequestBody PetDTO target){
        as.suspendUser(target.getId());
        return "Felhasználó felfüggesztve";
    }
    
    @PostMapping("/restoreuser")
    public String restoreUser(@RequestBody PetDTO target){
        as.restoreUser(target.getId());
        return "Felhasználó visszaállítva";
    }
    
}
