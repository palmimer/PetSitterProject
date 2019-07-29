/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progmatic.petsitterproject.services;

import com.progmatic.petsitterproject.controllers.AlreadyExistsException;
import com.progmatic.petsitterproject.entities.User;
import com.progmatic.petsitterproject.repositories.UserRepo;
import java.util.Random;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 *
 * @author imaginifer
 */
@Service
public class EmailService {
    
    private JavaMailSender sender;
    private UserRepo ur;
    private PasswordEncoder pwd;
    private final String cim = "kividrotposta@gmail.com";

    @Autowired
    public EmailService(JavaMailSender sender, UserRepo ur, PasswordEncoder pwd) {
        this.sender = sender;
        this.ur = ur;
        this.pwd = pwd;
    }
    
    public void sendSimpleTestMessage(){
        SimpleMailMessage smm = new SimpleMailMessage();
        smm.setFrom("kividrotposta@gmail.com");
        smm.setTo("povazsonk@gmail.com");
        smm.setSubject("Próba");
        smm.setText("Ha ezt megkaptad, a levelezés működik.");
        sender.send(smm);
    }
    
    public void sendFormattedTestMessage() throws MessagingException{
        MimeMessage msg = sender.createMimeMessage();
        MimeMessageHelper help = new MimeMessageHelper(msg, "utf-8");
        String txt = "<em>Ha ezt megkaptad,<br>a levelezés működik.</em>";
        help.setTo("povazson@seznam.cz");
        help.setFrom("kividrotposta@gmail.com");
        help.setSubject("Próba");
        help.setText(txt, true);
        sender.send(msg);
    }
    
    /*public void sendSimpleActivatorMessage(String userEmail){
        User u = (User)ur.loadUserByUsername(userEmail);
        String activator =  makeActivator(u.getId());
        SimpleMailMessage smm = new SimpleMailMessage();
        smm.setFrom(cim);
        smm.setTo(userEmail);
        smm.setSubject("Üdvözöl a KiVi közösség!");
        smm.setText("A felhasználói fiókod hitelesítéséhez kérjük, "
                + "kövesd a következő hivatkozást: "+
                "http://localhost:8080/verify?ver="+activator);
        sender.send(smm);
    }*/
    
    public void sendActivatorLink(String email) throws MessagingException, AlreadyExistsException{
        User u = (User)ur.loadUserByUsername(email);
        if(!u.getAuthorities().isEmpty()){
            throw new AlreadyExistsException("A fiók már aktív!");
        }
        MimeMessage msg = sender.createMimeMessage();
        MimeMessageHelper help = new MimeMessageHelper(msg, "utf-8");
        help.setTo(email);
        help.setFrom(cim);
        help.setSubject("Üdvözöl a KiVi közösség!");
        help.setText(makeActivatorMsg(u.getName(), makeActivator(u.getId())), true);
        sender.send(msg);
    }
    
    private String makeActivator(int ident){
        Random r = new Random();
        int chaff = r.nextInt(9000)+1000;
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
    public void activateUser(String val) throws AlreadyExistsException{
        User u;
        try{
            u = ur.findUser(disentangleActivator(val));
        }catch(EntityNotFoundException e){
           throw new AlreadyExistsException("Ilyen fiók nem létezik!");
        }
        u.setAuthorities(ur.findAuthority("ROLE_USER"));
    }
    
    public void sendReactivator(String email) throws AlreadyExistsException, MessagingException{
        if(!ur.userAlreadyExists(email)){
            throw new AlreadyExistsException("Ilyen fiók nem létezik!");
        }
        sendActivatorLink(email);
    }
    
    @Transactional
    public void passwordReset(String email) throws AlreadyExistsException, MessagingException{
        if(!ur.userAlreadyExists(email)){
            throw new AlreadyExistsException("Ilyen címről nem tudunk!");
        }
        User u = (User)ur.loadUserByUsername(email);
        String ersatz = String.valueOf(u.getDateOfJoin().hashCode());
        u.setPassword(pwd.encode(ersatz));
        u.setName("Válts jelszót "+u.getName()+"!");
        u.resetDateOfJoin();
        //sendSimplePasswordReset(email, ersatz);
        sendPasswordReset(email, ersatz, u.getName());
    }
    
    private void sendSimplePasswordReset(String email, String ersatz){
        SimpleMailMessage smm = new SimpleMailMessage();
        smm.setFrom(cim);
        smm.setTo(email);
        smm.setSubject("Pótjelszó");
        smm.setText(ersatz);
        sender.send(smm);
    }
    
    private void sendPasswordReset(String email, String ersatz, String name) throws MessagingException{
        MimeMessage msg = sender.createMimeMessage();
        MimeMessageHelper help = new MimeMessageHelper(msg, "utf-8");
        help.setTo(email);
        help.setFrom(cim);
        help.setSubject("Pótjelszó");
        help.setText(makePwdResetMsg(name, ersatz), true);
        sender.send(msg);
    }
    
    private String makeActivatorMsg(String name, String activator){
        String ms = "<p>Kedves "+name+"!</p><p>Szeretettel üdvözlünk a "
                + "kisállatvigyázó közösségben! Fiókod hitelesítéséhez "
                + "<a href=\"http://localhost:8080/verify?ver="+activator
                +"\">kattints ide!</a></p>";
        return ms;        
    }
    
    private String makePwdResetMsg(String name, String ersatz){
        String txt = "<p>Kedves "+name+"!</p><p>Rendszerünk ezt az ideiglenes jelszót "
                + "állította elő neked:</p><p style=\"margin-top:15px;margin-left:"
                + "40px;margin-bottom:15px\">"+ersatz
                +"</p><p>Használata után ajánlott a lehető leghamarabb új saját jelszót "
                + "beállítani a profil szerkesztése lehetőségnél!</p>";
        return txt;        
    }
    
    
    
    
    
    
     
     
}
