/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progmatic.petsitterproject.services;

import com.progmatic.petsitterproject.entities.Address;
import com.progmatic.petsitterproject.entities.Authority;
import com.progmatic.petsitterproject.entities.Availability;
import com.progmatic.petsitterproject.entities.PetType;
import com.progmatic.petsitterproject.entities.PlaceOfService;
import com.progmatic.petsitterproject.entities.Sitter;
import com.progmatic.petsitterproject.entities.SitterService;
import com.progmatic.petsitterproject.entities.User;
import com.progmatic.petsitterproject.entities.WorkingDay;
import com.progmatic.petsitterproject.repositories.UserRepo;
import java.time.LocalDate;
import java.util.*;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 *
 * @author imaginifer
 */
@Service
public class FillerService {
    
    private UserRepo ur;
    private PasswordEncoder pwd;

    @Autowired
    public FillerService(UserRepo ur, PasswordEncoder pwd) {
        this.ur = ur;
        this.pwd = pwd;
    }
    
    @Transactional
    public void fixDatabase(){
        makeAuthorities();
        makeDefaultUsers();
        makeDefaultAdmin();
        
    }
    
    @Transactional
    private void makeDefaultAdmin(){
        if(noAdmin()){
            User u = new User("admin", "adress@email.com", pwd.encode("super secret admin password"));
            u.setAuthorities(ur.findAuthority("ROLE_ADMIN"));
            ur.newUser(u);
        }
    }
    
    private void makeDefaultUsers(){
        if(ur.noUsers()){
            User u1 = new User("Techno Kolos", "techno.kolos@freemail.com", pwd.encode("password"));
            u1.setAuthorities(ur.findAuthority("ROLE_USER"));
            ur.newUser(u1);
            Sitter s1 = new Sitter("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.", u1);
            s1.setAddress(createAddress("Pocsajd", "Beton út 1.", 5555, s1));
            s1.setServices(newServiceList(PlaceOfService.OWNERS_HOME,PetType.CAT
                    ,1500, 6000, s1));
            s1.setAvailabilities(newCalendar(s1));
            ur.newSitter(s1);
            
            User u2 = new User("Tank Aranka", "tankari@citromail.com", pwd.encode("password"));
            u2.setAuthorities(ur.findAuthority("ROLE_USER"));
            ur.newUser(u2);
            Sitter s2 = new Sitter("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.", u2);
            s2.setAddress(createAddress("Békásmegyer", "Föld út 12.", 4593, s2));
            s2.setServices(newServiceList(PlaceOfService.SITTERS_HOME,PetType.DOG
                    ,900, 7000, s2));
            s2.setAvailabilities(newCalendar(s2));
            ur.newSitter(s2);
            
            User u3 = new User("Feles Elek", "feleselek@hotmail.com", pwd.encode("password"));
            u3.setAuthorities(ur.findAuthority("ROLE_USER"));
            ur.newUser(u3);
            
            User u4 = new User("Citad Ella", "citad.ella@yandex.com", pwd.encode("password"));
            u4.setAuthorities(ur.findAuthority("ROLE_USER"));
            ur.newUser(u4);
            Sitter s4 = new Sitter("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.", u4);
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
    private void makeAuthorities(){
        if(ur.absentAuthority("ROLE_USER")){
            ur.newAuthority(new Authority("ROLE_USER"));
        }
        if(ur.absentAuthority("ROLE_ADMIN")){
            ur.newAuthority(new Authority("ROLE_ADMIN"));
        }
    }
    
    private Set<SitterService> newServiceList(PlaceOfService place, PetType petType
            , int pricePerHour, int pricePerDay, Sitter s){
        Set<SitterService> listOfServices = new HashSet<>();
        SitterService ss = new SitterService(place,petType,pricePerHour, pricePerDay);
        ss.setSitter(s);
        ur.newService(ss);
        listOfServices.add(ss);
        return listOfServices;
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
    
}
