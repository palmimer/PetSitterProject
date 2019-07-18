/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progmatic.petsitterproject.services;

import com.progmatic.petsitterproject.controllers.AlreadyExistsException;
import com.progmatic.petsitterproject.dtos.RegistrationDTO;
import com.progmatic.petsitterproject.dtos.SearchCriteriaDTO;
import com.progmatic.petsitterproject.dtos.SitterDTO;
import com.progmatic.petsitterproject.entities.Address;
import com.progmatic.petsitterproject.entities.Availability;
import com.progmatic.petsitterproject.entities.PetType;
import com.progmatic.petsitterproject.entities.PlaceOfService;
import com.progmatic.petsitterproject.entities.Sitter;
import com.progmatic.petsitterproject.entities.SitterService;
import com.progmatic.petsitterproject.entities.User;
import static com.progmatic.petsitterproject.entities.User_.password;
import com.progmatic.petsitterproject.entities.WorkingDay;
import com.progmatic.petsitterproject.repositories.UserRepo;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    public UserService(UserRepo ur, PasswordEncoder pwd) {
        this.ur = ur;
        this.pwd = pwd;
        
    }
    
    @Transactional
    public void newPet(int userId, PetType petType, String name){
        if(!ur.isOwner(userId)){
            ur.newOwner(userId);
        }
        ur.newPet(userId, petType, name);
    }
    public User getUser(int userId){
        return ur.findUser(userId);
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
    
    
    public List<SitterDTO> filterSitters(SearchCriteriaDTO criteria){
        List<User> sitterUsers = searchResults(criteria.getName(), criteria.getPetType(), criteria.getPlaceOfService(), criteria.getPostCode());
        List<SitterDTO> petSitters = new ArrayList<>();
        for (User sitterUser : sitterUsers) {
            SitterDTO sitter = convertToDTO(sitterUser, sitterUser.getSitter());
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
        User newUser = new User(registration.getUsername(), registration.getEmail(), pwd.encode(registration.getPassword(), auth));
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
    
    private SitterDTO convertToDTO(User user, Sitter sitter) {
        SitterDTO response = new SitterDTO(
                sitter.getProfilePhoto(),
                sitter.getAddress(),
                sitter.getIntro(),
                sitter.getPetTypes(),
                sitter.getServices(),
                sitter.getAvailabilities()
        );
        return response;
    }
}


