package com.progmatic.petsitterproject.dtos;

import com.progmatic.petsitterproject.entities.Authority;
import com.progmatic.petsitterproject.entities.Owner;
import com.progmatic.petsitterproject.entities.Sitter;
import java.util.HashSet;
import java.util.Set;
import javax.validation.constraints.NotNull;

public class UserDTO {
    @NotNull
    private String name;
    @NotNull
    private String email;
    @NotNull
    private String password;
    private Owner owner;
    private Sitter sitter;
    private Set<Authority> authorities = new HashSet<>();

    public UserDTO(String name, String email, String password, Owner owner, Sitter sitter) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.owner = owner;
        this.sitter = sitter;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public Owner getOwner() {
        return owner;
    }

    public Sitter getSitter() {
        return sitter;
    }

    public Set<Authority> getAuthorities() {
        return authorities;
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

    public void setAuthorities(Set<Authority> authorities) {
        this.authorities = authorities;
    }
}
