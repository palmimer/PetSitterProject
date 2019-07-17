package com.progmatic.petsitterproject.entities;

public class User {

    private int id;

    private String name;

    private String email;

    private String password;

    private Owner owner;

    private Sitter sitter;

    public User(String name, String email, String password) {
    }

    public User(String name, String email, String password, Owner owner) {
    }

    public User(String name, String email, String password, Sitter sitter) {
    }

    public User(String name, String email, String password, Owner owner, Sitter sitter) {
    }
}
