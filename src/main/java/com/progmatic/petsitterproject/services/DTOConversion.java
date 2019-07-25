/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progmatic.petsitterproject.services;

import com.progmatic.petsitterproject.dtos.*;
import com.progmatic.petsitterproject.entities.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
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
//        response.setPetTypes(sitter.getPetTypes());
        response.setServices(convertToSitterServiceDTO(sitter.getServices()));
        response.setAvailabilities(convertCalendar( sitter.getAvailabilities() ) );
        response.setId(user.getId());
        
        return response;
    }
    
    
    public static SitterResponseDTO convertToSitterResponseDTO(User user, Sitter sitter) {
        SitterResponseDTO response = new SitterResponseDTO();
        response.setCity(sitter.getAddress().getCity());
        response.setAddress(sitter.getAddress().getAddress());
        response.setPostalCode(sitter.getAddress().getPostalCode());
        response.setIntro(sitter.getIntro());
//        response.setPetTypes(sitter.getPetTypes());
        response.setServices(convertToSitterServiceDTO(sitter.getServices()));
        response.setAvailabilities(convertCalendar(sitter.getAvailabilities()));
        
        return response;
    }
    public static TreeMap<LocalDate, Availability> convertCalendarToTreeMap(Set<WorkingDay> list){
        TreeMap<LocalDate, Availability> calendarView = new TreeMap<>();
        for (WorkingDay workingDay : list) {
            calendarView.put(workingDay.getwDay(), workingDay.getAvailability());
        }
//      /Comparator<WorkDayViewDTO> c = ((w1, w2) -> w1.getDate().compareTo(w2.getDate()));
//        calendarView.sort((w1, w2) -> w1.getDate().compareTo(w2.getDate()));
        return calendarView;
    }
    
    public static List<WorkDayViewDTO> convertCalendar(Set<WorkingDay> list){
        
        List<WorkDayViewDTO> calendarView = new ArrayList<>();
        for (WorkingDay wd : list) {
            calendarView.add(new WorkDayViewDTO(wd.getId()
                    , wd.getAvailability()
                    , wd.getwDay().format(DateTimeFormatter.ofPattern("yyyy.MM.dd"))));
        }
//        Comparator<WorkDayViewDTO> c = ((w1, w2) -> w1.getDate().compareTo(w2.getDate()));
        calendarView.sort((w1, w2) -> w1.getDate().compareTo(w2.getDate()));
        return calendarView;
    }
    
    public static Set<SitterServiceDTO> convertToSitterServiceDTO(Set<SitterService> list){
        Set<SitterServiceDTO> serviceView = new HashSet<>();
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
    
    
    public static Set<SitterServiceDTO> convertSetToSitterServiceDTO(Set<SitterService> sitterServices){
        Set<SitterServiceDTO> services = new HashSet<>();
        for (SitterService s : sitterServices) {
            SitterServiceDTO serv = new SitterServiceDTO();
            serv.setPetType(s.getPetType());
            serv.setPlace(s.getPlace());
            serv.setPricePerDay(s.getPricePerDay());
            serv.setPricePerHour(s.getPricePerHour());
            services.add(serv);
        }
        return services;
    }
    
    public static HashMap<String, PetType> convertToHashMap(List<Pet> pets){
        HashMap<String, PetType> petsInMap = new HashMap<>();
        for (Pet pet : pets) {
            petsInMap.put(pet.getName(), pet.getPetType());
        }
        return petsInMap;
    }

    private static List<SitterServiceDTO> convertSetToListSitterServiceDTO(Set<SitterService> services) {
        List<SitterServiceDTO> serviceDTOs = new ArrayList<>();
        for (SitterService service : services) {
            SitterServiceDTO ssdto = new SitterServiceDTO();
            ssdto.setPetType(service.getPetType());
            ssdto.setPlace(service.getPlace());
            ssdto.setPricePerDay(service.getPricePerDay());
            ssdto.setPricePerHour(service.getPricePerHour());
        }
        return serviceDTOs;
    }
    
    
}
