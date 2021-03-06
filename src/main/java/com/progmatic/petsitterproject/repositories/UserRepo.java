/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progmatic.petsitterproject.repositories;

import com.progmatic.petsitterproject.entities.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author progmatic
 */
@Repository
public class UserRepo implements UserDetailsService {

    @PersistenceContext
    EntityManager em;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
//        return em.createQuery("select u from User u where u.name =:nm", User.class)
//                .setParameter("nm", username).getSingleResult();
        return em.createQuery("select u from User u where u.email =:nm", User.class)
                .setParameter("nm", email).getSingleResult();
    }

    public byte[] getPhotoById(int ownerId) {
        ImageModel singleResult = (ImageModel) em.createQuery("select i from petsitter.image_model i where i.ownerId =: id").setParameter("id", ownerId).getSingleResult();
        return singleResult.getPic();
    }

    public boolean userAlreadyExists(String email) {
        return !em.createQuery("select u from User u where u.email = : e")
                .setParameter("e", email).getResultList().isEmpty();
    }

    public boolean noUsers() {
        return em.createQuery("select u from User u").getResultList().isEmpty();
    }

    @Transactional
    public void newPet(Pet p) {
        em.persist(p);
    }

    @Transactional
    public void newPets(Set<Pet> pets) {
        for (Pet pet : pets) {
            em.persist(pet);
        }
    }

    public boolean isOwner(int userId) {
        return findUser(userId).getOwner() != null;
    }

    @Transactional
    public void newOwner(Owner o) {
        em.persist(o);
    }

    public boolean isSitter(int userId) {
        return findUser(userId).getSitter() != null;
    }

    public boolean petExists(int petId) {
        return null != em.find(Pet.class, petId);
    }

    @Transactional
    public void newSitter(Sitter s) {
        em.persist(s);
    }

    @Transactional
    public void newAddress(Address a) {
        em.persist(a);
    }

    @Transactional
    public void newSitterService(SitterService ss) {
        em.persist(ss);
    }

    @Transactional
    public void newUser(User u) {
        em.persist(u);
    }

    public int getUserId(String email) {
        return em.createQuery("SELECT u.id FROM User u WHERE u.email = :email")
                .setParameter("email", email)
                .getFirstResult();
    }

    @Transactional
    public void deleteUser(User u) {
        em.remove(findUser(u.getId()));
    }

    public void deletePet(Pet p) {
        em.remove(findPet(p.getId()));
    }

    public void deleteSitterService(SitterService ss) {
        em.remove(findSitterService(ss.getId()));
    }

    public void deleteSitter(Sitter s) {
        em.remove(findSitter(s.getId()));
    }

    public void deleteOwner(Owner o) {
        em.remove(findOwner(o.getId()));
    }

    public User findUser(int id) {
        return em.find(User.class, id);
    }

    public Sitter findSitter(int id) {
        return em.find(Sitter.class, id);
    }

    public Pet findPet(int id) {
        return em.find(Pet.class, id);
    }

    public Pet findPet(String name, PetType petType) {
        return em.createQuery("SELECT p FROM Pet p WHERE p.name = :petName AND p.petType = :petType", Pet.class)
                .setParameter("petName", name)
                .setParameter("petType", petType)
                .getSingleResult();
    }

    public Owner findOwner(int id) {
        return em.find(Owner.class, id);
    }

    public Owner findOwnerByUserId(int userId) {
        User user = findUser(userId);
        return user.getOwner();
    }

    public Address findAddress(int id) {
        return em.find(Address.class, id);
    }

    public SitterService findSitterService(int id) {
        return em.find(SitterService.class, id);
    }

    public boolean sitterServiceExists(int id) {
        return null != em.find(SitterService.class, id);
    }

    public List<Sitter> getAllSitters() {
        return em.createQuery("select s from Sitter s").getResultList();
    }

    public List<User> getAllUsers() {
        return em.createQuery("select u from User u").getResultList();
    }

    public void newDay(WorkingDay w) {
        em.persist(w);
    }

    public WorkingDay findDay(int dayId) {
        return em.find(WorkingDay.class, dayId);
    }

    public void setDayAvail(int dayId, Availability avail) {
        em.find(WorkingDay.class, dayId).setAvailability(avail);
    }

    public void setDayDate(int dayId, LocalDate date) {
        em.find(WorkingDay.class, dayId).setwDay(date);
    }

    public Authority findAuthority(String authority) {
        return (Authority) em.createQuery("select a from Authority a where a.name = : au")
                .setParameter("au", authority).getSingleResult();
    }

    public boolean absentAuthority(String authority) {
        return em.createQuery("select a from Authority a where a.name = : au")
                .setParameter("au", authority).getResultList().isEmpty();
    }

    public void newAuthority(Authority a) {
        em.persist(a);
    }
    
    public void newSittingWork(SittingWork work){
        em.persist(work);
    }
    
    public SittingWork findSittingWork(int id){
        return em.find(SittingWork.class, id);
    }
    
    public boolean isRequestExpired(int id){
        return em.createQuery("select w from SittingWork w where w.id = : i")
                .setParameter("i", id).getResultList().isEmpty();
    }
    
    public List<SittingWork> getAllUnacceptedSittingWorks(){
        return em.createQuery("select w from SittingWork w where w.agreedOn = false")
                .getResultList();
    }
    
    public List<SittingWork> findSittingWorksByOwnerAndSitterId(int ownerId
            , int sitterId ){
        return em.createQuery("select w from SittingWork w where w.owner.id = : o"
                + " and w.sitter.id = : s")
                .setParameter("o", ownerId).setParameter("s", sitterId).getResultList();
                
    }
    
    public void deleteSittingWork(SittingWork w){
        em.remove(findSittingWork(w.getId()));
    }

//    public Sitter findSitterById(int id){
//        return (Sitter) em.createQuery("select i from image_model i where i.sitter_id =: 3")
//                .getSingleResult();
//    }
    public Sitter findSitterById(int id) {
        return em.find(Sitter.class, id);
    }

//    public ImageModel getImage(int id) {
//        return em.find(ImageModel.class, Long.valueOf(id));
//    }
}
