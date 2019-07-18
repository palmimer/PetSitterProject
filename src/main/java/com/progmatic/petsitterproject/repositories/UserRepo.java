/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progmatic.petsitterproject.repositories;

import com.progmatic.petsitterproject.entities.*;
import java.time.LocalDate;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

/**
 *
 * @author progmatic
 */
@Repository
public class UserRepo implements UserDetailsService{
    
    @PersistenceContext
    EntityManager em;
    
    private PasswordEncoder pwd;

    @Autowired
    public UserRepo(PasswordEncoder pwd) {
        this.pwd = pwd;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return (UserDetails)em.createQuery("select u from User where u.username =: nm")
                .setParameter("nm", username).getSingleResult();
    }
    
    public void newService(int userId, PlaceOfService place, PetType type, int pricePerHour, int pricePerDay){
        SitterService srv = new SitterService(place, type, pricePerHour, pricePerDay);
        em.persist(srv);
        findUser(userId).getSitter().getServices().add(srv);
    }
    
    public void newPet(int userId, PetType petType, String name){
        Pet p = new Pet(petType,name);
        em.persist(p);
        findUser(userId).getOwner().setPets(p);
    }
    
    public boolean isOwner(int userId){
        return findUser(userId).getOwner() != null;
    }
    
    public void newOwner(int userId){
        Owner o = new Owner();
        em.persist(o);
        findUser(userId).setOwner(o);
    }
    
    public boolean isSitter(int userId){
        return findUser(userId).getSitter() != null;
    }
    
    public void newSitter(int userId, Byte[] profilePhoto, Address address
            , String intro, List<PetType> petTypes, List<SitterService> services
            , List<WorkingDay> availabilities){
        Sitter s = new Sitter(profilePhoto, address, intro, petTypes, services, availabilities);
        em.persist(s);
        findUser(userId).setSitter(s);
        
    }
    
    public Address newAddress(String city, String address, int postalCode){
        Address a = new Address(city, address, postalCode);
        em.persist(a);
        return a;
    }
    
    public void newUser(String name, String email, String password){
        em.persist(new User(name, email, pwd.encode(password)));
    }
    
    
    public User findUser(int id){
       return em.find(User.class, id);
    }
    
    public List<User> getAllSitters(){
        return em.createQuery("select u from User where u.sitter != null")
                .getResultList();
    }
    
    public List<User> getAllUsers(){
        return em.createQuery("select u from User").getResultList();
    }
    
    public WorkingDay newDay(LocalDate date){
        WorkingDay w = new WorkingDay(date, Availability.FREE);
        em.persist(w);
        return w;
    }
    
    public WorkingDay findDay(int dayId){
        return em.find(WorkingDay.class, dayId);
    }
    
    
    
}
