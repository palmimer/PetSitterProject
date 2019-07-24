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
    private CalendarUpdater cu;
    
    @Autowired
    public UserService(UserRepo ur, PasswordEncoder pwd, CalendarUpdater cu) {
        this.ur = ur;
        this.pwd = pwd;
        this.cu = cu;
        startBackGroundTasks();
    }
    
    private void startBackGroundTasks(){
        cu.start();
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
        
    }
    
    public User getUser(int userId){
        return ur.findUser(userId);
    }
    
    @Transactional
    public void registerNewSitter(SitterRegistrationDTO sd){
        User u = getCurrentUser();
        Sitter s = new Sitter(/*sd.getProfilePhoto(),*/ sd.getIntro(), u);
        s.setAddress(createAddress(sd.getCity(), sd.getAddress(), sd.getPostalCode(), s));
        s.setServices(newServiceList(sd.getPlace(),sd.getPetType()
                ,sd.getPricePerHour(), sd.getPricePerDay(), s));
        s.setAvailabilities(newCalendar(s));
        u.setSitter(s);
        ur.newSitter(s);
    }
    
    private List<SitterService> newServiceList(PlaceOfService place, PetType petType
            , int pricePerHour, int pricePerDay, Sitter s){
        List<SitterService> listOfServices = new ArrayList<>();
        SitterService ss = new SitterService(place,petType,pricePerHour, pricePerDay);
        ss.setSitter(s);
        ur.newService(ss);
        listOfServices.add(ss);
        return listOfServices;
    }
    
    @Transactional
    public void registerNewService(SitterServiceDTO ssrv){
        Sitter current = getCurrentUser().getSitter();
        SitterService ss = new SitterService(ssrv.getPlace(),ssrv.getPetType()
                ,ssrv.getPricePerHour(), ssrv.getPricePerDay());
        ss.setSitter(current);
        //current.getServices().add(ss);
        ur.newService(ss);
    }
    
    @Transactional
    public Address createAddress(String city, String address, int postalCode, Sitter s){
        Address a = new Address(city, address, postalCode, s);
        ur.newAddress(a);
        return a;
    }
    
    @Transactional
    private List<WorkingDay> newCalendar(Sitter s){
        LocalDate d = LocalDate.now();
        List<WorkingDay> cal = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            WorkingDay w = new WorkingDay(d, Availability.FREE);
            w.setSitter(s);
            cal.add(w);
            ur.newDay(w);
            d = d.plusDays(1);
        }
        return cal;
    }
    
    @Transactional
    public void editProfile(ProfileEditDTO edit){
        User u = (User) ur.findUser(getCurrentUser().getId());
        u.setName(edit.getUsername());
        u.setPassword(pwd.encode(edit.getPassword()));
        Sitter s = ur.findSitter(u.getSitter().getId());
        s.setIntro(edit.getIntro());
        s.setProfilePhoto(edit.getProfilePhoto());
        Address a = ur.findAddress(s.getAddress().getId());
        a.setAddress(edit.getAddress());
        a.setCity(edit.getCity());
        a.setPostalCode(edit.getPostalCode());
    }
    
    @Transactional
    public void setWorkingDay(LocalDate day, Availability avail){
        Sitter s = getCurrentUser().getSitter();
        for (WorkingDay w : s.getAvailabilities()) {
            if(w.getwDay().isEqual(day)){
                ur.findDay(w.getId()).setAvailability(avail);
                break;
            }
        }
    }
    
    public List<SitterViewDTO> filterSitters(SearchCriteriaDTO criteria){
        List<Sitter> sitterUsers = searchResults(criteria.getName(), criteria.getPetType(), criteria.getPlaceOfService(), criteria.getPostCode());
        List<SitterViewDTO> petSitters = new ArrayList<>();
        for (Sitter sitterUser : sitterUsers) {
            SitterViewDTO sitter = DTOConversion.convertToSitterViewDTO(sitterUser.getUser(), sitterUser);
            petSitters.add(sitter);
        }
        return petSitters;
    }
    @Transactional
    public void createUser(RegistrationDTO registration) throws AlreadyExistsException {
        if (ur.userAlreadyExists(registration.getEmail())) {
            throw new AlreadyExistsException("Ilyen e-mailcím már létezik az adatbázisban!");
        }
        //Authority auth = ur.findAuthority("ROLE_USER");
        User newUser = new User(registration.getUsername(), registration.getEmail(), pwd.encode(registration.getPassword()));
        //newUser.setAuthorities(auth);
        ur.newUser(newUser);
    }
    
    @Transactional
    public void suspendAccount(){
        User u = ur.findUser(getCurrentUser().getId());
        u.getAuthorities().clear();
    }
    
    private SitterService createServiceWithoutPrice(PlaceOfService place, PetType petType) {
        return new SitterService(place, petType);
        }
    
    private List<Sitter> searchResults(String name, PetType pet, PlaceOfService pl, int postal){
        List<Sitter> sitters = ur.getAllSitters();
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
    
    private List<Sitter> filterByName(String name, List<Sitter> list){
        return list.stream().filter(s -> s.getUser().getName().contains(name))
                .collect(Collectors.toList());
    }
    
    private List<Sitter> filterByPetType(PetType p, List<Sitter> list){
        return list.stream().filter(s -> s.getPetTypes()
                .contains(p)).collect(Collectors.toList());
    }
    
    private List<Sitter> filterByPlace(PlaceOfService p, List<Sitter> list){
        return list.stream().filter(s -> s.getServices().stream()
                .anyMatch(srv -> srv.getPlace()==p)).collect(Collectors.toList());
    }
    
    private List<Sitter> filterByPostal(int postal, List<Sitter> list){
        return list.stream().filter(s -> s.getAddress()
                .getPostalCode()==postal).collect(Collectors.toList());
    }
    
    private User getCurrentUser(){
        return (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
    
    //TODO remove
//    public ImageModel image(int id) {
//        return ur.getImage(id);
//    }

//    public byte[] getUserImage(int ownerId) {
//        return ;
//    }
//    
    
}


