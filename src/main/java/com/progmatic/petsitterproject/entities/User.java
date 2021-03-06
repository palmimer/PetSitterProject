package com.progmatic.petsitterproject.entities;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import static javax.persistence.CascadeType.REMOVE;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@Table(name = "tbl_user")
public class User implements Serializable, UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String email;
    private String password;
    @OneToOne
    private ImageModel profilePhoto;
    @OneToOne(cascade = REMOVE, mappedBy = "user")
    private Owner owner;
    @OneToOne(cascade = REMOVE, mappedBy = "user")
    private Sitter sitter;
    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Authority> authorities;
    private LocalDateTime dateOfJoin;

    public User() {
    }

    public User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.authorities = new HashSet<>();
        dateOfJoin = LocalDateTime.now();
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public Owner getOwner() {
        return owner;
    }

    public Sitter getSitter() {
        return sitter;
    }

    public ImageModel getProfilePhoto() {
        return profilePhoto;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    public void setSitter(Sitter sitter) {
        this.sitter = sitter;
    }

    public void setAuthorities(Authority authority) {
        this.authorities.add(authority);
    }

    public void setProfilePhoto(ImageModel profilePhoto) {
        this.profilePhoto = profilePhoto;
    }

    public LocalDateTime getDateOfJoin() {
        return dateOfJoin;
    }
    
    public void resetDateOfJoin(){
        dateOfJoin = LocalDateTime.now();
    } 
    
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return name;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
