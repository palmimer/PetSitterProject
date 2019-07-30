/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progmatic.petsitterproject.services;

import com.progmatic.petsitterproject.entities.Availability;
import com.progmatic.petsitterproject.entities.Sitter;
import com.progmatic.petsitterproject.entities.User;
import com.progmatic.petsitterproject.entities.WorkingDay;
import com.progmatic.petsitterproject.repositories.UserRepo;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author imaginifer
 */
@Service
public class DataBaseMaintenance{
    
    private LocalDate reference;
    private UserRepo ur;
    private FillerService fs;
    
    @Autowired
    public DataBaseMaintenance(UserRepo ur, FillerService fs){
        this.ur = ur;
        this.fs = fs;
        reference = LocalDate.of(1970, 1, 1);
    }
    
    @Scheduled(fixedRate = 900000)
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void runTask() {
        LocalDate present = LocalDate.now();
        if(reference.getYear() == 1970){
            fs.fixDatabase();
        }
        if(reference.isBefore(present)){
            reference = present;
            updateCalendars(present);
            removeExpiredUsers();
        }
    }
    
    
    private void updateCalendars(LocalDate date){
        List<Sitter> sitters = ur.getAllSitters();
        if (!sitters.isEmpty()){
            //System.out.println(users.size());
            for (Sitter s : sitters) {
                rollCalendar(s.getAvailabilities(), date);
            }
        }
    }
    
    @Transactional
    private void rollCalendar(Set<WorkingDay> cal, LocalDate date){
        LocalDate last = findLast(cal);
        LocalDate now = date;
        for (WorkingDay w : cal) {
            if(w.getwDay().isBefore(now)){
                last=last.plusDays(1);
                System.out.println("utolsó: "+last);
                ur.setDayDate(w.getId(), last);
                ur.setDayAvail(w.getId(), Availability.FREE);
            }
        }
    }
    
    private LocalDate findLast(Set<WorkingDay> cal){
        return cal.stream().map(d -> d.getwDay()).max((d1, d2) -> d1.compareTo(d2)).get();
    }
    
    @Transactional
    private void removeExpiredUsers(){
        List<User> users = ur.getAllUsers();
        LocalDateTime deadline = LocalDateTime.now().minusHours(48);
        for (User u : users) {
            if(u.getAuthorities().isEmpty() 
                    && u.getDateOfJoin().isBefore(deadline)){
                System.out.println("Lejárt: "+u.getName());
                ur.deleteUser(u);
            }
            if(u.getName().contains("Válts jelszót") 
                    && u.getDateOfJoin().isBefore(deadline)){
                u.getAuthorities().clear();
            }
        }
    }
    
    
}
