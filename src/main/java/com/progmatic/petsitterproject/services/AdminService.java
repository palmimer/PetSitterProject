/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progmatic.petsitterproject.services;

import com.progmatic.petsitterproject.entities.User;
import com.progmatic.petsitterproject.repositories.UserRepo;
import java.util.Random;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

/**
 *
 * @author imaginifer
 */
@Service
public class AdminService {
    
    private UserRepo ur;
    

    @Autowired
    public AdminService(UserRepo ur) {
        this.ur = ur;
    }
    
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public void suspendUser(int id){
        User u = ur.findUser(id);
        if(!u.getAuthorities().isEmpty()){
            u.getAuthorities().clear();
            u.resetDateOfJoin();
            Random r = new Random();
            int chaff = r.nextInt(9000)+1000;
            StringBuilder sb = new StringBuilder();
            sb.append(chaff).append(u.getEmail());
            u.setEmail(sb.toString());
        }
    }
    
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public void restoreUser(int id){
        User u = ur.findUser(id);
        if(u.getAuthorities().isEmpty()){
            u.setAuthorities(ur.findAuthority("ROLE_USER"));
            u.setEmail(u.getEmail().substring(4));
        }
    }
    
    
    
}
