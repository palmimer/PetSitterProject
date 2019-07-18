/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progmatic.petsitterproject.services;

import com.progmatic.petsitterproject.dtos.SearchCriteriaDTO;
import com.progmatic.petsitterproject.dtos.SitterDTO;
import com.progmatic.petsitterproject.entities.User;
import com.progmatic.petsitterproject.repositories.UserRepo;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author progmatic
 */
@Service
public class UserService {
    
    private UserRepo ur;

    @Autowired
    public UserService(UserRepo ur) {
        this.ur = ur;
    }
    
    public User getUser(int userId){
        return ur.findUser(userId);
    }
    
    public List<SitterDTO> filterSitters(SearchCriteriaDTO criteria){
        List<User> sitterUsers = ur.getAllSitters();
        List<Sitter> petsitters = ur;
        return petsitters;
    }
}
