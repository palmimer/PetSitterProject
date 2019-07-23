/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progmatic.petsitterproject;

import com.progmatic.petsitterproject.entities.Address;
import com.progmatic.petsitterproject.entities.ImageModel;
import com.progmatic.petsitterproject.entities.Sitter;
import com.progmatic.petsitterproject.entities.SitterService;
import com.progmatic.petsitterproject.entities.WorkingDay;
import com.progmatic.petsitterproject.repositories.ImageRepository;
import java.io.InputStream;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.io.ClassPathResource;

/**
 *
 * @author imaginifer
 */
@SpringBootApplication
@ComponentScan("com.progmatic.petsitterproject")
public class Main /*implements CommandLineRunner*/ {

    @Autowired
    ImageRepository imageRepository;

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

//    @Override
//    public void run(String... arg0) throws Exception {
//        
//        ClassPathResource imgPath = new ClassPathResource("images/blackhole.jpg");
//        byte[] arrayPic = new byte[(int) imgPath.contentLength()];
//        imgPath.getInputStream().read(arrayPic);
//        ImageModel pic = new ImageModel(1, "Black Hole", "jpg", arrayPic);
//        Sitter feri = new Sitter(null, null, null, null, null);
//        feri.setProfilePhoto(pic);
//        imageRepository.save(pic);
//        
////        InputStream binaryStream = resultSet.getBinaryStream(yourBlobColumnIndex);
//    }
}
