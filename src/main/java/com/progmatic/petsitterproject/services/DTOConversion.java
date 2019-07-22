/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progmatic.petsitterproject.services;

import com.progmatic.petsitterproject.dtos.*;
import com.progmatic.petsitterproject.entities.*;
import java.time.LocalDate;
import java.util.*;

/**
 *
 * @author imaginifer
 */
public class DTOConversion {
    
    public static SitterViewDTO convertToSitterViewDTO(User user, Sitter sitter) {
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
    
    public TreeMap<LocalDate, Availability> convertCalendar(List<WorkingDay> list){
        TreeMap<LocalDate, Availability> calendarView = new TreeMap<>();
        for (WorkingDay wd : list) {
            calendarView.put(wd.getwDay(), wd.getAvailability());
        }
        return calendarView;
    }
    
    public List<SitterServiceDTO> convertSitterService(List<SitterService> list){
        List<SitterServiceDTO> serviceView = new ArrayList<>();
        for (SitterService s : list) {
            SitterServiceDTO sv = new SitterServiceDTO();
            sv.setPetType(s.getPetType());
            sv.setPlace(s.getPlace());
            sv.setPricePerDay(s.getPricePerDay());
            sv.setPricePerHour(s.getPricePerHour());
            serviceView.add(sv);
        }
        return serviceView;
    }
    
    
}
