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
import java.util.HashMap;
import java.util.Map;
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
    public Map<String, Object> activateUser(@RequestParam("ver") String valid) throws AlreadyExistsException{
        es.activateUser(valid);
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Sikeres hitelesítés!");
        return response;
    }
    
    @GetMapping("/suspendaccount")
    public Map<String, Object> removeSelf(){
        us.suspendAccount();
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Fiókodat felfüggesztettük. Ha meggondolod magad, 48 órán belül visszaállíthatod!");
        return response;
    }
    
    @PostMapping("/restoreaccount")
    public Map<String, Object> restoreSelf(@RequestBody ResetDTO req) throws AlreadyExistsException, MessagingException{
        es.sendReactivator(req.getEmail());
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Fiókodat visszaállítottuk!");
        return response;
    }
    
    @GetMapping("/filler")
    public String fillers(){
        fs.fixDatabase();
        return "Megtöltve!";
    }
    
    @PostMapping("/resetpassword")
    public Map<String, Object> resetPassword(@RequestBody ResetDTO req) throws AlreadyExistsException, MessagingException{
        es.passwordReset(req.getEmail());
        Map<String, Object> response = new HashMap<>();
        response.put("message", "A pótjelszót elküldtük a megadott címre!");
        return response;
    }
    
    @PostMapping("/suspenduser")
    public Map<String, Object> suspendUser(@RequestBody PetDTO target){
        as.suspendUser(target.getId());
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Felhasználó felfüggesztve");
        return response;
    }
    
    @PostMapping("/restoreuser")
    public Map<String, Object> restoreUser(@RequestBody PetDTO target){
        as.restoreUser(target.getId());
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Felhasználó visszaállítva");
        return response;
    }
    
    @PostMapping("/requestsitting")
    public Map<String, Object> requestSitting(@RequestBody PetDTO target) throws AlreadyExistsException, MessagingException{
        es.sittingRequest(target.getId());
        Map<String, Object> response = new HashMap<>();
        response.put("message", "A kérést elküldtük a választott KiVinek!");
        return response;
    }
    
    @GetMapping("/acceptwork")
    public Map<String, Object> acceptWork(@RequestParam("n") String nr) throws MessagingException, AlreadyExistsException{
        es.acceptRequest(nr);
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Köszönjük, hogy elvállaltad a munkát!");
        return response;
    }
    
    
}
