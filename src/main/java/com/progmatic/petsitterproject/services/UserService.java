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
    private ConversionService conv;

    @Autowired
    public UserService(UserRepo ur, PasswordEncoder pwd, ConversionService conv) {
        this.ur = ur;
        this.pwd = pwd;
        this.conv = conv;
        
    }
    
    @Transactional
    public void registerNewOwner(PetType petType, String name){
        User u = getCurrentUser();
        if(u.getOwner()== null){
            Owner o = new Owner();
            ur.newOwner(o);
            u.setOwner(o);
        }
        Pet p = new Pet(petType,name);
        ur.newPet(p);
        u.getOwner().setPets(p);
    }
    
    public User getUser(int userId){
        return ur.findUser(userId);
    }
    
    @Transactional
    public void registerNewSitter(SitterRegistrationDTO sd){
        Sitter s = new Sitter(sd.getProfilePhoto(), createAddress(sd.getCity()
                , sd.getAddress(), sd.getPostalCode()), sd.getIntro()
                , sd.getPetTypes(), registerNewServiceList(sd.getServices()), newCalendar());
        getCurrentUser().setSitter(s);
        ur.newSitter(s);
    }
    
    private List<SitterService> registerNewServiceList(List<SitterServiceDTO> ssrv){
        List<SitterService> listOfServices = new ArrayList<>();
        for (SitterServiceDTO s : ssrv) {
            SitterService ss = new SitterService(s.getPlace(),s.getPetType()
                ,s.getPricePerHour(), s.getPricePerDay());
            ur.newService(ss);
            listOfServices.add(ss);
        }
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


