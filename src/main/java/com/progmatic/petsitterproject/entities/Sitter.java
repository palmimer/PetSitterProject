package com.progmatic.petsitterproject.entities;


import java.util.List;
import java.util.Map;

public class Sitter {

    private Byte[] profilePhoto;

    private Address address;

    private String intro;

    private List<PetType> petTypes;

    private List<Service> services;

    private int id;

    private Map availabilities;
}
