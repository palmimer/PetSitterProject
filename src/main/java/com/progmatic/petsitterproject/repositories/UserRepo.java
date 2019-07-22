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
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;

/**
 *
 * @author progmatic
 */
@Repository
public class UserRepo implements UserDetailsService{
    
    @PersistenceContext
    EntityManager em;
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return (User)em.createQuery("select u from User u where u.name =: nm")
                .setParameter("nm", username).getSingleResult();
    }
    
    public boolean userAlreadyExists(String email){
        return !em.createQuery("select u from User u where u.email = : e")
                .setParameter("e", email).getResultList().isEmpty();
    }
    
    public void newService(SitterService srv){
        em.persist(srv);
    }
    
    public void newPet(Pet p){
        em.persist(p);
    }
    
    public boolean isOwner(int userId){
        return findUser(userId).getOwner() != null;
    }
    
    public void newOwner(Owner o){
        em.persist(o);
    }
    
    public boolean isSitter(int userId){
        return findUser(userId).getSitter() != null;
    }
    
    public void newSitter(Sitter s){
        em.persist(s);
        }
    
    public void newAddress(Address a){
        em.persist(a);
    }
    
    public void newSitterService(SitterService ss){
        em.persist(ss);
    }
    
    public void newUser(User u){
        em.persist(u);
    }
    
    public void deleteUser(int id){
        em.remove(findUser(id));
    }
    
    public void deletePet(int id){
        em.remove(findPet(id));
    }
    
    public void deleteSitterService(int id){
        em.remove(findSitterService(id));
    }
    
    public User findUser(int id){
       return em.find(User.class, id);
    }
    
    public Pet findPet(int id){
        return em.find(Pet.class, id);
    }
    
    public SitterService findSitterService(int id){
        return em.find(SitterService.class, id);
    }
    
    public List<User> getAllSitters(){
        return em.createQuery("select u from User u where u.sitter != null")
                .getResultList();
    }
    
    public List<User> getAllUsers(){
        return em.createQuery("select u from User u").getResultList();
    }
    
    public void newDay(WorkingDay w){
        em.persist(w);
    }
    
    public WorkingDay findDay(int dayId){
        return em.find(WorkingDay.class, dayId);
    }
    
    public Authority findAuthority(String authority){
        return (Authority) em.createQuery("select a from Authority a where a.name = : au")
                .setParameter("au", authority).getSingleResult();
    }
    
    public boolean absentAuthority(String authority){
        return em.createQuery("select a from Authority a where a.name = : au")
                .setParameter("au", authority).getResultList().isEmpty();
    }
    
    public void newAuthority(Authority a){
        em.persist(a);
    }
    
    
    
}
