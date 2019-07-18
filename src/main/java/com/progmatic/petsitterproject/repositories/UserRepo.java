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
    
    public void newService(int userId, PlaceOfService place, TypeOfService type, int pricePerHour, int pricePerDay){
        Service srv = new Service(place, type, pricePerHour, pricePerDay);
        em.persist(srv);
        findUser(userId).getSitter().getServices().add(srv);
    }
    
    public void newPet(int userId, PetType petType, String name){
        User u = findUser(userId);
        if(u.getOwner()==null){
            newOwner(u);
        }
        Pet p = new Pet(petType,name);
        em.persist(p)
        u.getOwner().setPets(p);
    }
    
    private Owner newOwner(User u){
        Owner o = new Owner();
        em.persist(o);
        return o;
    }
    
    public void newSitter(int userId, Byte[] profilePhoto, Address address
            , String intro, List<PetType> petTypes, List<Service> services
            , List<WorkingDay> availabilities){
        Sitter s = new Sitter(profilePhoto, address, intro, petTypes, services);
        em.persist(s);
        findUser(userId).setSitter(s);
        
    }
    
    private Address newAddress(String city, String address, int postalCode){
        Address a = new Address(city, address, postalCode);
        em.persist(a);
        return a;
    }
    
    public void newUser(){
        
    }
    
    
    public User findUser(int id){
       return em.find(User.class, id);
    }
    
    public List<User> getAllSitters(){
        return em.createQuery("select u from User join fetch u.sitter != null")
                .getResultList();
    }
    
    public List<User> getAllUsers(){
        return em.createQuery("select u from User").getResultList();
    }
    
    public void setDate(int id, LocalDate date){
        
    }
    
    public WorkingDay newDay(LocalDate date){
        
    }
    
    
    
    
}
