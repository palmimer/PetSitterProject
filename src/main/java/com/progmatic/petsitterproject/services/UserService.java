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
    
    @Transactional
    public void registerNewOwner(String email, Set<PetDTO> petsToRegister){
        User user = (User) ur.loadUserByUsername(email);
        if( user.getOwner() == null ){
            Owner owner = new Owner();
            owner.setUser(user);
            user.setOwner(owner);
            ur.newOwner(owner);
        }
        Owner owner = user.getOwner();
        for (PetDTO petToRegister : petsToRegister) {
            Pet pet = new Pet(petToRegister.getPetType(), petToRegister.getName());
            pet.setOwner(owner);
            ur.newPet(pet);
        }
    }
    
    //Overloadolás, ha utólag akar azzá válni  
    @Transactional
    public void registerNewOwner(Set<PetDTO> petsToRegister){
        User user = getCurrentUser();
        if( user.getOwner() == null ){
            Owner owner = new Owner();
            owner.setUser(user);
            user.setOwner(owner);
            ur.newOwner(owner);
        }
        Owner owner = user.getOwner();
        for (PetDTO petToRegister : petsToRegister) {
            Pet pet = new Pet(petToRegister.getPetType(), petToRegister.getName());
            pet.setOwner(owner);
            ur.newPet(pet);
        }    
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
    public void registerNewSitter(String email, SitterRegistrationDTO sd){
        User user = (User) ur.loadUserByUsername(email);
        Sitter s = new Sitter( sd.getIntro(), user);
        s.setProfilePhoto(sd.getProfilePhoto());
        s.setAddress(createAddress(sd.getCity(), sd.getAddress(), sd.getPostalCode(), s));
        s.setServices(newServiceSet(sd.getServices(), s));
        s.setAvailabilities(newCalendar(s));
        user.setSitter(s);
        ur.newSitter(s);
    }
    
    //Overloadolás, ha utólag akar azzá válni
    @Transactional
    public void registerNewSitter(SitterRegistrationDTO sd){
        User user = getCurrentUser();
        Sitter s = new Sitter( sd.getIntro(), user);
        s.setProfilePhoto(sd.getProfilePhoto());
        s.setAddress(createAddress(sd.getCity(), sd.getAddress(), sd.getPostalCode(), s));
        s.setServices(newServiceSet(sd.getServices(), s));
        s.setAvailabilities(newCalendar(s));
        user.setSitter(s);
        ur.newSitter(s);
    }
    
    private Set<SitterService> newServiceSet(Set<SitterServiceDTO> srv, Sitter s){
        Set<SitterService> listOfServices = new HashSet<>();
        if (s.getServices() != null) {
            listOfServices = s.getServices();
        }
        for (SitterServiceDTO dto : srv) {
            SitterService ss = new SitterService(dto.getPlace(), dto.getPetType()
                    , dto.getPricePerHour(), dto.getPricePerDay());
            ss.setSitter(s);
            ur.newService(ss);
            listOfServices.add(ss);
        }
        return listOfServices;
    }
    
    @Transactional
    public void registerNewService(int userId, SitterServiceDTO ssrv){
        User user = getCurrentUser();
        Sitter current = user.getSitter();
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
    public void editProfile(UserDTO edit){
        User u = (User) ur.findUser(getCurrentUser().getId());
        u.setName(edit.getName());
        u.setEmail(edit.getEmail());
        if(edit.getOwnerData() == null || edit.getOwnerData().getPets().isEmpty()){
            ur.deleteOwner(u.getOwner());
        } else {
            editPets(edit.getOwnerData().getPets());
        }
        if(edit.getSitterData() == null){
            ur.deleteSitter(u.getSitter());
        } else {
            Sitter s = ur.findSitter(u.getSitter().getId());
            s.setIntro(edit.getSitterData().getIntro());
            //s.setProfilePhoto(edit.getSitterData().getProfilePhoto());
            Address a = ur.findAddress(s.getAddress().getId());
            a.setAddress(edit.getSitterData().getAddress());
            a.setCity(edit.getSitterData().getCity());
            a.setPostalCode(edit.getSitterData().getPostalCode());
        }
    }
    
    private void editPets(Set<PetDTO> pets){
        for (PetDTO pet : pets) {
            
        }
    }
    
    private void editSitter(){
        
    }
    
    @Transactional
    public void setWorkingDay(int dayId, Availability avail){
        ur.setDayAvail(dayId, avail);
    }
    
    @Transactional
    public void removePet(PetDTO pet){
        Set<Pet> pets = getCurrentUser().getOwner().getPets();
        List<Pet> all = new ArrayList<>();
        for (Pet p : pets) {
            if(p.getName().equals(pet.getName()) && p.getPetType() == pet.getPetType()){
                all.add(p);
            }
        }
        ur.deletePet(all.get(0));
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
    public void createUser(UserRegistrationDTO userData) throws AlreadyExistsException {
        if (ur.userAlreadyExists(userData.getEmail())) {
            throw new AlreadyExistsException("Ilyen e-mailcím már létezik az adatbázisban!");
        }
        //Authority auth = ur.findAuthority("ROLE_USER");
        User newUser = new User(userData.getUsername(), userData.getEmail(), pwd.encode(userData.getPassword()));
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
    
    public User getCurrentUser(){
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


