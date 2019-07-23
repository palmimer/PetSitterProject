/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progmatic.petsitterproject.repositories;

import com.progmatic.petsitterproject.dtos.SearchCriteriaDTO;
import com.progmatic.petsitterproject.entities.*;
import java.util.ArrayList;
import java.util.List;
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
    
    public List<Sitter> searchSitters(SearchCriteriaDTO criteria){
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Sitter> cq = cb.createQuery(Sitter.class);
        Root<Sitter> sitter = cq.from(Sitter.class);
        List<Predicate> predicates = new ArrayList<>();
        if(!criteria.getName().isEmpty()){
            Predicate p = cb.equal(sitter.get(Sitter_.user).get(User_.name), criteria.getName());
            predicates.add(p);
        }
        if (criteria.getPetType() != null){
            Predicate p = cb.equal(sitter.join(Sitter_.services)
                    .get(SitterService_.petType), criteria.getPetType());
            predicates.add(p);
        }
        if (criteria.getPlaceOfService() != null){
            Predicate p = cb.equal(sitter.join(Sitter_.services)
                    .get(SitterService_.place), criteria.getPlaceOfService());
            predicates.add(p);
        }
        if (criteria.getPostCode() != 0){
            Predicate p = cb.equal(sitter.get(Sitter_.address)
                    .get(Address_.postalCode), criteria.getPostCode());
            predicates.add(p);
        }
        
        cq.select(sitter).where(predicates.toArray(new Predicate[predicates.size()]));
        return em.createQuery(cq).getResultList();
    }
    
}
