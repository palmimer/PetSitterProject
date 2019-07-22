/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progmatic.petsitterproject.services;

import com.progmatic.petsitterproject.controllers.AlreadyExistsException;
import com.progmatic.petsitterproject.dtos.*;
import com.progmatic.petsitterproject.entities.*;
import com.progmatic.petsitterproject.repositories.UserRepo;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 *
 * @author progmatic
 */
@Service
public class UserService {
    
    private UserRepo ur;
    private PasswordEncoder pwd;
    private DTOConversionService conv;

    @Autowired
    public UserService(UserRepo ur, PasswordEncoder pwd, DTOConversionService conv) {
        this.ur = ur;
        this.pwd = pwd;
        this.conv = conv;
        
    }
    
    @Transactional
    public void registerNewOwner(PetType petType, String name){
        User user = getCurrentUser();
        // ha még nem volt a user tulajdonos, = nincs owner objektuma,
        if( user.getOwner() == null ){
            // akkor létrehozunk egy owner objektumot
            Owner owner = new Owner();
            // beállítjuk rajta a usert
            owner.setUser(user);
            // beállítjuk a usernek ezt az ownert
            user.setOwner(owner);
            // beírjuk az adatbázisba az új ownert
            ur.newOwner(owner);
        } 
        // létrehozunk egy új Pet-et a beküldött paraméterekkel
        Pet pet = new Pet(petType, name);
        // ennek a Pet-nek beállítjuk owner-nek az aktuális user owner-ét
        Owner owner = user.getOwner();
        pet.setOwner(owner);
        // beírjuk az adatbázisba az új Pet-et
        ur.newPet(pet);
        //owner.setPets(pet);
        //ur.newUser(user);
    }
    
    public User getUser(int userId){
        return ur.findUser(userId);
    }
    
    @Transactional
    public void registerNewSitter(SitterRegistrationDTO sd){
        Sitter s = new Sitter(sd.getProfilePhoto(), createAddress(sd.getCity(),
                sd.getAddress(), sd.getPostalCode()), sd.getIntro(), /*sd.getPetTypes(),*/ 
                newServiceList( sd.getPlace(),sd.getPetType(),sd.getPricePerHour(), sd.getPricePerDay() ),
                newCalendar());
        User user = getCurrentUser();
        s.setUser(user);
        user.setSitter(s);
        ur.newSitter(s);
    }
    
    private List<SitterService> newServiceList(PlaceOfService place, PetType petType
            , int pricePerHour, int pricePerDay){
        List<SitterService> listOfServices = new ArrayList<>();
        SitterService ss = new SitterService(place,petType,pricePerHour, pricePerDay);
        ur.newService(ss);
        listOfServices.add(ss);
        return listOfServices;
    }
    
    @Transactional
    public void registerNewService(SitterServiceDTO ssrv){
        SitterService ss = new SitterService(ssrv.getPlace(),ssrv.getPetType()
                ,ssrv.getPricePerHour(), ssrv.getPricePerDay());
        ur.newService(ss);
        getCurrentUser().getSitter().getServices().add(ss);
    }
    
    @Transactional
    public Address createAddress(String city, String address, int postalCode){
        Address a = new Address(city, address, postalCode);
        ur.newAddress(a);
        return a;
    }
    
    @Transactional
    private List<WorkingDay> newCalendar(){
        LocalDate d = LocalDate.now();
        List<WorkingDay> cal = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            cal.add(ur.newDay(d));
            d = d.plusDays(1);
        }
        return cal;
    }
    
    @Transactional
    public void updateCalendars(){
        List<User> users = ur.getAllSitters();
        for (User user : users) {
            rollCalendar(user.getSitter().getAvailabilities());
        }
    }
    
    private List<WorkingDay> rollCalendar(List<WorkingDay> cal){
        LocalDate last = findLast(cal);
        LocalDate now = LocalDate.now();
        for (WorkingDay w : cal) {
            if(w.getwDay().isBefore(now)){
                last=last.plusDays(1);
                w.setwDay(last);
                w.setAvailability(Availability.FREE);
            }
        }
        return cal;
    }
    
    private LocalDate findLast(List<WorkingDay> cal){
        return cal.stream().map(d -> d.getwDay()).max((d1, d2) -> d1.compareTo(d2)).get();
    }
    
    
    public List<SitterViewDTO> filterSitters(SearchCriteriaDTO criteria){
        List<User> sitterUsers = searchResults(criteria.getName(), criteria.getPetType(), criteria.getPlaceOfService(), criteria.getPostCode());
        List<SitterViewDTO> petSitters = new ArrayList<>();
        for (User sitterUser : sitterUsers) {
            SitterViewDTO sitter = conv.convertToSitterViewDTO(sitterUser, sitterUser.getSitter());
            petSitters.add(sitter);
        }
        return petSitters;
    }
    @Transactional
    public void createUser(RegistrationDTO registration) throws AlreadyExistsException {
        if (ur.userAlreadyExists(registration.getEmail())) {
            throw new AlreadyExistsException("Ilyen e-mailcím már létezik az adatbázisban!");
        }
        Authority auth = ur.findAuthority("ROLE_USER");
        User newUser = new User(registration.getUsername(), registration.getEmail(), pwd.encode(registration.getPassword()));
        newUser.setAuthorities(auth);
        ur.newUser(newUser);
    }
    
    private SitterService createServiceWithoutPrice(PlaceOfService place, PetType petType) {
        return new SitterService(place, petType);
        }
    
    private List<User> searchResults(String name, PetType pet, PlaceOfService pl, int postal){
        List<User> sitters = ur.getAllSitters();
        if(!name.isEmpty()){
            sitters = filterByName(name, sitters);
        }
        if (pet != null){
            sitters = filterByPetType(pet, sitters);
        }
        if (pl != null){
            sitters = filterByPlace(pl, sitters);
        }
        if (postal != 0){
            sitters = filterByPostal(postal, sitters);
        }
        return sitters;
    }
    
    private List<User> filterByName(String name, List<User> list){
        return list.stream().filter(u -> u.getName().contains(name))
                .collect(Collectors.toList());
    }
    
    private List<User> filterByPetType(PetType p, List<User> list){
        return list.stream().filter(u -> u.getSitter().getPetTypes()
                .contains(p)).collect(Collectors.toList());
    }
    
    private List<User> filterByPlace(PlaceOfService p, List<User> list){
        return list.stream().filter(u -> u.getSitter().getServices().stream()
                .anyMatch(s -> s.getPlace()==p)).collect(Collectors.toList());
    }
    
    private List<User> filterByPostal(int postal, List<User> list){
        return list.stream().filter(u -> u.getSitter().getAddress()
                .getPostalCode()==postal).collect(Collectors.toList());
    }
    
    private User getCurrentUser(){
        return (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}


