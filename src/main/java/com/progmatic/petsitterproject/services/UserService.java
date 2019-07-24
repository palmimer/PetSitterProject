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
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
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
    public void fixDatabase(){
        makeAuthorities();
        makeDefaultUsers();
        makeDefaultAdmin();
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
    
    public UserDTO getUserDTO(){
        User user = getCurrentUser();
        UserDTO userDTO = new UserDTO(user);
        if(user.getOwner() != null){
            userDTO.setOwnerData(new OwnerDTO(user.getOwner()));
        }
        if(user.getSitter() != null){
            userDTO.setSitterData(DTOConversion.convertToSitterResponseDTO(user, user.getSitter()));
        }
        return userDTO;
    }
    
    @Transactional
    public void registerNewSitter(SitterRegistrationDTO sd){
        User u = getCurrentUser();
        Sitter s = new Sitter(/*sd.getProfilePhoto(),*/ sd.getIntro(), u);
        s.setAddress(createAddress(sd.getCity(), sd.getAddress(), sd.getPostalCode(), s));
        s.setServices(newServiceList(sd.getPlace(), sd.getPetType(), sd.getPricePerHour(), sd.getPricePerDay(), s));
        s.setAvailabilities(newCalendar(s));
        u.setSitter(s);
        ur.newSitter(s);
    }
    
    private Set<SitterService> newServiceList(PlaceOfService place, PetType petType
            , int pricePerHour, int pricePerDay, Sitter s){
        Set<SitterService> listOfServices = new HashSet<>();
        if (s.getServices() != null) {
            listOfServices = s.getServices();
        }
        SitterService ss = new SitterService(place, petType, pricePerHour, pricePerDay);
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
    private Set<WorkingDay> newCalendar(Sitter s){
        LocalDate d = LocalDate.now();
        Set<WorkingDay> cal = new HashSet<>();
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
    private void makeAuthorities(){
        if(ur.absentAuthority("ROLE_USER")){
            ur.newAuthority(new Authority("ROLE_USER"));
        }
        if(ur.absentAuthority("ROLE_ADMIN")){
            ur.newAuthority(new Authority("ROLE_ADMIN"));
        }
    }
    
    @Transactional
    private void makeDefaultAdmin(){
        if(noAdmin()){
            User u = new User("admin", "adress@email.com", "super secret admin password");
            u.setAuthorities(ur.findAuthority("ROLE_ADMIN"));
            ur.newUser(u);
        }
    }
    
    private void makeDefaultUsers(){
        if(ur.noUsers()){
            User u1 = new User("Techno Kolos", "techno.kolos@freemail.com", "password");
            u1.setAuthorities(ur.findAuthority("ROLE_USER"));
            ur.newUser(u1);
            Sitter s1 = new Sitter("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.", u1);
            s1.setAddress(createAddress("Pocsajd", "Beton út 1.", 5555, s1));
            s1.setServices(newServiceList(PlaceOfService.OWNERS_HOME,PetType.CAT
                    ,1500, 6000, s1));
            s1.setAvailabilities(newCalendar(s1));
            ur.newSitter(s1);
            
            User u2 = new User("Tank Aranka", "tankari@citromail.com", "password");
            u2.setAuthorities(ur.findAuthority("ROLE_USER"));
            ur.newUser(u2);
            Sitter s2 = new Sitter("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.", u2);
            s2.setAddress(createAddress("Békásmegyer", "Föld út 12.", 4593, s2));
            s2.setServices(newServiceList(PlaceOfService.SITTERS_HOME,PetType.DOG
                    ,900, 7000, s2));
            s2.setAvailabilities(newCalendar(s2));
            ur.newSitter(s2);
            
            User u3 = new User("Feles Elek", "feleselek@hotmail.com", "password");
            u3.setAuthorities(ur.findAuthority("ROLE_USER"));
            ur.newUser(u3);
            
            User u4 = new User("Citad Ella", "citad.ella@yandex.com", "password");
            u4.setAuthorities(ur.findAuthority("ROLE_USER"));
            ur.newUser(u4);
            Sitter s4 = new Sitter("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.", u2);
            s4.setAddress(createAddress("Pocsajd", "Beton út 16.", 5555, s4));
            s4.setServices(newServiceList(PlaceOfService.OWNERS_HOME,PetType.BIRD
                    ,800, 4500, s4));
            s4.setAvailabilities(newCalendar(s4));
            ur.newSitter(s4);
        }
    }
    
    private boolean noAdmin(){
        List<User> users = ur.getAllUsers();
        for (User user : users) {
            for (GrantedAuthority authority : user.getAuthorities()) {
                if(authority.getAuthority().equals("ROLE_ADMIN")){
                    return false;
                }
            }
        }
        return true;
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
        Authority auth = ur.findAuthority("ROLE_USER");
        User newUser = new User(registration.getUsername(), registration.getEmail(), pwd.encode(registration.getPassword()));
        newUser.setAuthorities(auth);
        ur.newUser(newUser);
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


