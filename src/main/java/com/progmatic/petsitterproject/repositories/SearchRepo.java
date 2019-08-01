/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progmatic.petsitterproject.repositories;

import com.progmatic.petsitterproject.entities.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.springframework.stereotype.Repository;

/**
 *
 * @author imaginifer
 */
@Repository
public class SearchRepo {
    
    @PersistenceContext
    EntityManager em;
    
    public List<Sitter> searchSitters(String name, PetType pet, PlaceOfService pl, int postal){
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Sitter> cq = cb.createQuery(Sitter.class);
        Root<Sitter> sitter = cq.from(Sitter.class);
        List<Predicate> predicates = new ArrayList<>();
        Predicate def = cb.isNotEmpty(sitter.get(Sitter_.user).get(User_.authorities));
        predicates.add(def);
        if(!name.isEmpty()){
            Predicate p = cb.equal(cb.lower(sitter.get(Sitter_.user).get(User_.name)), name.toLowerCase());
            predicates.add(p);
        }
        if (pet != null){
            Predicate p = cb.equal(sitter.join(Sitter_.services)
                    .get(SitterService_.petType), pet);
            predicates.add(p);
        }
        if (pl != null){
            Predicate p = cb.equal(sitter.join(Sitter_.services)
                    .get(SitterService_.place), pl);
            predicates.add(p);
        }
        if (postal != 0){
            Predicate p = cb.equal(sitter.get(Sitter_.address)
                    .get(Address_.postalCode), postal);
            predicates.add(p);
        }
        
        cq.select(sitter).where(predicates.toArray(new Predicate[predicates.size()]));
        return em.createQuery(cq).getResultList();
    }
    
    public List<Sitter> searchResults(String name, PetType pet, PlaceOfService pl, int postal){
        List<Sitter> sitters = em.createQuery("select s from Sitter s join fetch user where s.user.authorities is not empty").getResultList();
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

    
}
