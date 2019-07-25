/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progmatic.petsitterproject.services;

import com.progmatic.petsitterproject.entities.User;
import com.progmatic.petsitterproject.repositories.UserRepo;
import java.util.Random;
import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 *
 * @author imaginifer
 */
@Service
public class EmailService {
    
    private JavaMailSender sender;
    private UserRepo ur;

    @Autowired
    public EmailService(JavaMailSender sender, UserRepo ur) {
        this.sender = sender;
        this.ur = ur;
    }
    
    public void sendSimpleTestMessage(){
        SimpleMailMessage smm = new SimpleMailMessage();
        smm.setFrom("kividrotposta@gmail.com");
        smm.setTo("povazsonk@gmail.com");
        smm.setSubject("Próba");
        smm.setText("Ha ezt megkaptad, a levelezés működik.");
        sender.send(smm);
    }
    
    public void sendSimpleActivatorMessage(String userEmail){
        String activator =  makeActivator(userEmail);
        SimpleMailMessage smm = new SimpleMailMessage();
        smm.setFrom("kividrotposta@gmail.com");
        smm.setTo(userEmail);
        smm.setSubject("Üdvözöl a KiVi közösség!");
        smm.setText("A felhasználói fiókod hitelesítéséhez kérjük, "
                + "kövesd a következő hivatkozást: "+
                "http://localhost:8080/verify?ver="+activator);
        sender.send(smm);
    }
    
    private String makeActivator(String email){
        User u = (User)ur.loadUserByUsername(email);
        Random r = new Random();
        int chaff = r.nextInt(9000)+1000;
        int ident = u.getId();
        StringBuilder sb = new StringBuilder();
        sb.append(chaff).append(ident);
        int kvant = Integer.parseInt(sb.toString())*17;
        return String.valueOf(kvant);
    }
    
    private int disentangleActivator(String valid){
        int q = Integer.parseInt(valid)/17;
        String s = String.valueOf(q).substring(4);
        //System.out.println("érték: "+s);
        return Integer.parseInt(s);
    }
    
    @Transactional
    public void activateUser(String val){
        User u;
        try{
            u = ur.findUser(disentangleActivator(val));
        }catch(EntityNotFoundException e){
            return;
        }
        u.setAuthorities(ur.findAuthority("ROLE_USER"));
    }
    
     
     
}
