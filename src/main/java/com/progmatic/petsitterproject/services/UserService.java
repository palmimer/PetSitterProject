/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progmatic.petsitterproject.services;

import com.progmatic.petsitterproject.entities.*;
import com.progmatic.petsitterproject.repositories.UserRepo;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.transaction.Transactional;
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
    
    @Transactional
    public void newPet(int userId, PetType petType, String name){
        if(!ur.isOwner(userId)){
            ur.newOwner(userId);
        }
        ur.newPet(userId, petType, name);
    }
    
    @Transactional
    public void newSitter(int userId, Byte[] profilePhoto, Address address
            , String intro, List<PetType> petTypes, List<SitterService> services){
            
        ur.newSitter(userId, profilePhoto, address, intro, petTypes, services, newCalendar());
    }
    
    @Transactional
    private List<WorkingDay> newCalendar(){
        LocalDate d = LocalDate.now();
        List<WorkingDay> cal = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            cal.add(ur.newDay(d));
            d=d.plusDays(1);
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
    
    
}
