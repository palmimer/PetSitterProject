/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progmatic.petsitterproject.services;

import com.progmatic.petsitterproject.dtos.*;
import com.progmatic.petsitterproject.entities.*;
import org.springframework.stereotype.Service;

/**
 *
 * @author imaginifer
 */
@Service
public class ConversionService {
    
    public SitterViewDTO convertToSitterViewDTO(User user, Sitter sitter) {
        SitterViewDTO response = new SitterViewDTO();
        response.setProfilePhoto(sitter.getProfilePhoto());
        response.setUserName(user.getName());
        response.setCity(sitter.getAddress().getCity());
        response.setAddress(sitter.getAddress().getAddress());
        response.setPostalCode(sitter.getAddress().getPostalCode());
        response.setIntro(sitter.getIntro());
        response.setPetTypes(sitter.getPetTypes());
        response.setServices(sitter.getServices());
        response.setAvailabilities(sitter.getAvailabilities());
        
        return response;
    }
}
