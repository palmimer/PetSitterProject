package com.progmatic.petsitterproject.entities;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

@Entity
@Table(name = "image_model")
public class ImageModel implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int sitterId;
    private String name;
    private String type;
    @Lob
    private byte[] pic;

    public ImageModel() {
    }

    public ImageModel(int sitterId, String name, String type, byte[] pic) {
        this.sitterId = sitterId;
        this.name = name;
        this.type = type;
        this.pic = pic;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public byte[] getPic() {
        return this.pic;
    }

    public void setPic(byte[] pic) {
        this.pic = pic;
    }

    public int getSitterId() {
        return sitterId;
    }

    public void setSitterId(int sitterId) {
        this.sitterId = sitterId;
    }
}
