/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progmatic.petsitterproject.repositories;

import com.progmatic.petsitterproject.entities.*;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
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
        return (User)em.createQuery("select u from User where u.name =: nm")
                .setParameter("nm", username).getSingleResult();
    }
    
    public void newService(){
        
    }
    
    public void newPet(){
        
    }
    
    public void newOwner(){
        
    }
    
    public void newSitter(){
        
    }
    
    private Address newAddress(){
        return new Address();
    }
    
    public void newUser(){
        
    }
    
    
    public User findUserAsSitter(String username){
        return (User) em.createQuery("select u from User join fetch u.sitter where u.sitter. =: nm")
                .setParameter("nm", username).getSingleResult();
    }
    
    
    
    
    
}
