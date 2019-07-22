/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progmatic.petsitterproject.services;

import com.progmatic.petsitterproject.entities.Availability;
import com.progmatic.petsitterproject.entities.User;
import com.progmatic.petsitterproject.entities.WorkingDay;
import com.progmatic.petsitterproject.repositories.UserRepo;
import java.time.LocalDate;
import java.util.List;
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
        List<User> users = ur.getAllSitters();
        if (!users.isEmpty()){
            for (User user : users) {
                rollCalendar(user.getSitter().getAvailabilities(), date);
            }
        }
    }
    
    private List<WorkingDay> rollCalendar(List<WorkingDay> cal, LocalDate date){
        LocalDate last = findLast(cal);
        LocalDate now = date;
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
