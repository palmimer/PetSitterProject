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
import java.util.*;

/**
 *
 * @author imaginifer
 */
public class DTOConversion {

    public static SitterViewDTO convertToSitterViewDTO(User user, Sitter sitter) {
        SitterViewDTO response = new SitterViewDTO();
        response.setUserName(user.getName());
        response.setCity(sitter.getAddress().getCity());
        response.setAddress(sitter.getAddress().getAddress());
        response.setPostalCode(sitter.getAddress().getPostalCode());
        response.setIntro(sitter.getIntro());
        response.setServices(convertSetToSitterServiceDTO(sitter.getServices()));
        response.setAvailabilities(convertCalendar(sitter.getAvailabilities()));
        response.setId(user.getId());
        response.setAverageRating(sitter.getAverageRating());
        response.setNumberOfRatings(sitter.getNumberOfRatings());
        return response;
    }

    public static SitterResponseDTO convertToSitterResponseDTO(User user, Sitter sitter) {
        SitterResponseDTO response = new SitterResponseDTO();
        response.setCity(sitter.getAddress().getCity());
        response.setAddress(sitter.getAddress().getAddress());
        response.setPostalCode(sitter.getAddress().getPostalCode());
        response.setIntro(sitter.getIntro());
//        response.setPetTypes(sitter.getPetTypes());
        response.setServices(convertSetToSitterServiceDTO(sitter.getServices()));
        response.setAvailabilities(convertCalendar(sitter.getAvailabilities()));

        return response;
    }
    
    public static SitterRegistrationDTO convertToSitterRegistrationDTO(SitterViewDTO sitterData){
        SitterRegistrationDTO newSitterData = new SitterRegistrationDTO();
        newSitterData.setIntro(sitterData.getIntro());
        newSitterData.setCity(sitterData.getCity());
        newSitterData.setAddress(sitterData.getAddress());
        newSitterData.setPostalCode(sitterData.getPostalCode());
        newSitterData.setServices(sitterData.getServices());
        return newSitterData;
    }
    
    public static List<WorkDayViewDTO> convertCalendar(Set<WorkingDay> list){
        
        List<WorkDayViewDTO> calendarView = new ArrayList<>();
        for (WorkingDay wd : list) {
            calendarView.add(new WorkDayViewDTO(wd.getId(),
                     wd.getAvailability(),
                     wd.getwDay().format(DateTimeFormatter.ofPattern("yyyy.MM.dd"))));
        }
//        Comparator<WorkDayViewDTO> c = ((w1, w2) -> w1.getDate().compareTo(w2.getDate()));
        calendarView.sort((w1, w2) -> w1.getDate().compareTo(w2.getDate()));
        return calendarView;
    }
    
    public static Set<SitterService> convertDTOToSitterService(Set<SitterServiceDTO> serviceDTOs){
        Set<SitterService> services = new HashSet<>();
        for (SitterServiceDTO sdto : serviceDTOs) {
            SitterService sv = new SitterService();
            sv.setPetType(sdto.getPetType());
            sv.setPlace(sdto.getPlace());
            sv.setPricePerDay(sdto.getPricePerDay());
            sv.setPricePerHour(sdto.getPricePerHour());
            services.add(sv);
        }
        return services;
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
    
    public static AccountViewDTO convertToAccountViewDTO(){
        AccountViewDTO view = new AccountViewDTO();
        
        return view;
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
    
    public static Set<Pet> convertPetDTOsToPets(Set<PetDTO> petDTOs){
        Set<Pet> pets = new HashSet<>();
        for (PetDTO petDTO : petDTOs) {
            Pet pet = new Pet(petDTO.getPetType(), petDTO.getName());
            pets.add(pet);
        }
        return pets;
    }
    
    public static Set<PetDTO> convertPetsToPetDTOs(Set<Pet> pets){
        Set<PetDTO> petDTOs = new HashSet<>();
        for (Pet pet : pets) {
            PetDTO petDTO = new PetDTO(pet.getName(), pet.getPetType());
            petDTOs.add(petDTO);
        }
        return petDTOs;
    }
    
}
