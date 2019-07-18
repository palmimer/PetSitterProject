/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progmatic.petsitterproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;


/**
 *
 * @author imaginifer
 */
@SpringBootApplication
@ComponentScan("com.progmatic.petsitterproject")
public class Main {
    
    
    public static void main(String[] args){
        SpringApplication.run(Main.class, args);
    }
}


