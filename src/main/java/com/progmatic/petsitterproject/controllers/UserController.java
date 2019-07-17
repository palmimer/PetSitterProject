/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progmatic.petsitterproject.controllers;

import com.progmatic.petsitterproject.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author progmatic
 */
@RestController
public class UserController {
    
    private UserService us;

    @Autowired
    public UserController(UserService us) {
        this.us = us;
    }
    
    
}
