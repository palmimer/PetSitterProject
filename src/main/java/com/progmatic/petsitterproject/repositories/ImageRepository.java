package com.progmatic.petsitterproject.repositories;
 
import com.progmatic.petsitterproject.entities.ImageModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<ImageModel, Long>{
}
