/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progmatic.petsitterproject.services;

import com.progmatic.petsitterproject.entities.Availability;
import com.progmatic.petsitterproject.entities.Sitter;
import com.progmatic.petsitterproject.entities.WorkingDay;
import com.progmatic.petsitterproject.repositories.UserRepo;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author imaginifer
 */
@Component
public class CalendarUpdater extends Thread{
    
    private LocalDate reference;
    private UserRepo ur;
    
    @Autowired
    public CalendarUpdater(UserRepo ur){
        this.ur = ur;
        reference = LocalDate.of(1970, 1, 1);
    }

    @Override
    @Transactional
    public void run() {
        while(true){
            LocalDate present = LocalDate.now();
            if(reference.isBefore(present)){
                reference = present;
                updateCalendars(present);
            }
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
    
    private Set<WorkingDay> rollCalendar(Set<WorkingDay> cal, LocalDate date){
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
        return cal;
    }
    
    private LocalDate findLast(Set<WorkingDay> cal){
        return cal.stream().map(d -> d.getwDay()).max((d1, d2) -> d1.compareTo(d2)).get();
    }
    
    
}
