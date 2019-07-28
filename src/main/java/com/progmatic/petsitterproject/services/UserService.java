/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progmatic.petsitterproject.services;

import com.progmatic.petsitterproject.controllers.AlreadyExistsException;
import com.progmatic.petsitterproject.dtos.*;
import com.progmatic.petsitterproject.entities.*;
import com.progmatic.petsitterproject.repositories.ImageRepository;
import com.progmatic.petsitterproject.repositories.UserRepo;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 *
 * @author progmatic
 */
@Service
public class UserService {

    private UserRepo ur;
    private PasswordEncoder pwd;
    private CalendarUpdater cu;
    private ImageRepository imageRepository;

    @Autowired
    public UserService(UserRepo ur, PasswordEncoder pwd, CalendarUpdater cu, ImageRepository imageRepository) {
        this.ur = ur;
        this.pwd = pwd;
        this.cu = cu;
        this.imageRepository = imageRepository;
        startBackGroundTasks();
    }

    private void startBackGroundTasks() {
        cu.start();
    }
//    
//    
//    @Transactional
//    public void registerNewOwner(PetType petType, String name){
//        User user = getCurrentUser();
//        // ha még nem volt a user tulajdonos, = nincs owner objektuma,
//        if( user.getOwner() == null ){
//            // akkor létrehozunk egy owner objektumot
//            Owner owner = new Owner();
//            // beállítjuk rajta a usert
//            owner.setUser(user);
//            // beállítjuk a usernek ezt az ownert
//            user.setOwner(owner);
//            // beírjuk az adatbázisba az új ownert
//            ur.newOwner(owner);
//        } 
//        // létrehozunk egy új Pet-et a beküldött paraméterekkel
//        Pet pet = new Pet(petType, name);
//        // ennek a Pet-nek beállítjuk owner-nek az aktuális user owner-ét
//        Owner owner = user.getOwner();
//        pet.setOwner(owner);
//        // beírjuk az adatbázisba az új Pet-et
//        ur.newPet(pet);
//    }

    @Transactional
    public void registerNewOwner(String email, Set<PetDTO> petsToRegister){
        User user = (User) ur.loadUserByUsername(email);
        if (user.getOwner() == null) {
            Owner owner = new Owner();
            owner.setUser(user);
            user.setOwner(owner);
            ur.newOwner(owner);
        }
        Owner owner = user.getOwner();
        for (PetDTO petToRegister : petsToRegister) {
            Pet pet = new Pet(petToRegister.getPetType(), petToRegister.getName());
            pet.setOwner(owner);
            ur.newPet(pet);
        }
    }
    
    @Transactional
    public User getUser(int userId) {
        return ur.findUser(userId);
    }
    
    @Transactional
    public UserDTO getUserDTO() {
        //User user = getCurrentUser();
        User user = getUser(getCurrentUser().getId());
        UserDTO userDTO = new UserDTO(user);
        if (user.getOwner() != null) {
            userDTO.setOwnerData(new OwnerDTO(user.getOwner()));
        }
        if (user.getSitter() != null) {
            userDTO.setSitterData(DTOConversion.convertToSitterResponseDTO(user, user.getSitter()));
        }
        return userDTO;
    }

    @Transactional
    public void registerNewSitter(String email, SitterRegistrationDTO sd) {
        User user = (User) ur.loadUserByUsername(email);
        Sitter s = new Sitter(sd.getIntro(), user);
        s.setAddress(createAddress(sd.getCity(), sd.getAddress(), sd.getPostalCode(), s));
        s.setServices(registerNewServiceSet(sd.getServices(), s));
        s.setAvailabilities(newCalendar(s));
        user.setSitter(s);
        ur.newSitter(s);
    }
    
    @Transactional
    private Set<SitterService> registerNewServiceSet(Set<SitterServiceDTO> srv, Sitter s) {
        Set<SitterService> listOfServices = new HashSet<>();
        if (s.getServices() != null) {
            listOfServices = s.getServices();
        }
        for (SitterServiceDTO dto : srv) {
            SitterService ss = new SitterService(dto.getPlace(), dto.getPetType(),
                    dto.getPricePerHour(), dto.getPricePerDay());
            ss.setSitter(s);
            ur.newSitterService(ss);
            listOfServices.add(ss);
        }
        return listOfServices;
    }

//    @Transactional
//    public void registerNewService(int userId, SitterServiceDTO ssrv) {
//        User user = getCurrentUser();
//        Sitter current = user.getSitter();
//        SitterService ss = new SitterService(ssrv.getPlace(), ssrv.getPetType(),
//                ssrv.getPricePerHour(), ssrv.getPricePerDay());
//        ss.setSitter(current);
//        //current.getServices().add(ss);
//        ur.newService(ss);
//    }

    @Transactional
    public Address createAddress(String city, String address, int postalCode, Sitter s) {
        Address a = new Address(city, address, postalCode, s);
        ur.newAddress(a);
        return a;
    }

    @Transactional
    private Set<WorkingDay> newCalendar(Sitter s) {
        LocalDate d = LocalDate.now();
        Set<WorkingDay> cal = new HashSet<>();
        for (int i = 0; i < 30; i++) {
            WorkingDay w = new WorkingDay(d, Availability.FREE);
            w.setSitter(s);
            cal.add(w);
            ur.newDay(w);
            d = d.plusDays(1);
        }
        return cal;
    }

    @Transactional
    public void editProfile(ProfileEditDTO editedProfile){
        User u = (User) ur.findUser(getCurrentUser().getId());
        modifyBasicUserData(editedProfile);
        // ha a usernek eddig volt owner objektuma, de a beérkezett adatokban most már nincs owner adat
        if(ur.isOwner(u.getId()) && editedProfile.getOwnerData() == null){
            ur.deleteOwner(u.getOwner());
            // volt owner és érkezik adat az Owner objektumban
        } else if(ur.isOwner(u.getId()) && editedProfile.getOwnerData() != null){
            // elküldjük az új állatlistát és a userId-t szerkesztésre
            editPets(editedProfile.getOwnerData().getPets(), u.getId());
        } else {
            registerNewOwner(u.getEmail(), editedProfile.getOwnerData().getPets());
        }
        // ha a usernek eddig volt sitter objektuma, de a beérkezett adatokban most már nincs sitter adat
        if(ur.isSitter(u.getId()) && editedProfile.getSitterData() == null){
            ur.deleteSitter(u.getSitter());
        // ha a usernek nem volt sittere és most van sitter adat, akkor új sittert regisztrálunk
        } else if (!ur.isSitter(u.getId()) && editedProfile.getSitterData() != null) {
            SitterRegistrationDTO sitterData = DTOConversion.convertToSitterRegistrationDTO(editedProfile.getSitterData());
            registerNewSitter(u.getEmail(), sitterData);
        // ha volt sittere és most is van sitter adat, 
        } else {
            // elővesszük a régi sitterét
            Sitter s = ur.findSitter(u.getSitter().getId());
            modifySitterData(s, editedProfile.getSitterData());
        }
        
    }
    
    @Transactional
    private void modifyBasicUserData(ProfileEditDTO editedProfile){
        User u = (User) ur.findUser(getCurrentUser().getId());
        u.setName(editedProfile.getUsername());
        u.setPassword(pwd.encode(editedProfile.getPassword()));
        u.setEmail(editedProfile.getEmail());
    }
    
    @Transactional
    private void modifySitterData(Sitter s, SitterViewDTO newSitterData){
        s.setIntro(newSitterData.getIntro());
        setNewAddress(s, newSitterData);
        
        // eredeti sitterservices kitörlése
        for (SitterService originalService : s.getServices()) {
            ur.deleteSitterService(originalService);
        }
        Set<SitterService> editedServices = DTOConversion.convertDTOToSitterService(newSitterData.getServices());
        // beírjuk az új serviceket az adatbázisba
        for (SitterService editedService : editedServices) {
            editedService.setSitter(s);
            ur.newSitterService(editedService);
        }
        // beállítjuk az új service-eket a sitternek
        s.setServices(editedServices);
        
        // TODO itt kéne a naptármódosítás még
    }
    
    @Transactional
    private void setNewAddress(Sitter s, SitterViewDTO newSitterData){
        Address a = ur.findAddress(s.getAddress().getId());
        a.setAddress(newSitterData.getAddress());
        a.setCity(newSitterData.getCity());
        a.setPostalCode(newSitterData.getPostalCode());
    }
    
    @Transactional
    private void editPets(Set<PetDTO> pets, int userId) {
        Set<PetDTO> newSetOfPetDTOs = new HashSet<>();
        Owner owner = getUser(userId).getOwner();
        Set<PetDTO> oldPetDTOs = findOldPetsAndConvertToDTO(userId);
        // kikeressük az új állatokat a régivel összehasonlítva
        Set<PetDTO> newPets = findNewPets(pets, oldPetDTOs);
        // hozzáadjuk az új állatokat a listához
        newSetOfPetDTOs.addAll(newPets);
        Set<Pet> newSetOfPets = DTOConversion.convertPetDTOsToPets(newSetOfPetDTOs);
        // létrehozzuk az új állatokat az adatbázisban
        for (Pet newPet : newSetOfPets) {
            newPet.setOwner(owner);
        }
        ur.newPets(newSetOfPets);
        // kikeressük a régi állatokat, ami már nincs az új listában
        Set<PetDTO> oldPetsToDelete = findObsoletePets(pets, oldPetDTOs);
        // a törlendő állatokat kitöröljük az adatbázisból
        for (PetDTO oldPetToDelete : oldPetsToDelete) {
            Pet petToDelete = ur.findPet(oldPetToDelete.getName(), oldPetToDelete.getPetType());
            ur.deletePet(petToDelete);
        }
        // az ownernek beállítjuk ezeket az új állatokat
        owner.setPets(newSetOfPets);
    }
    
    private Set<PetDTO> findNewPets(Set<PetDTO> pets, Set<PetDTO> oldPets) {
        Set<PetDTO> newPets = new HashSet<>();
        // végignézzük az új listát, megnézzük benne van-e a régiben
        for (PetDTO pet : pets) {
            for (PetDTO oldPet : oldPets) {
                if ( ( oldPet.getName().equals(pet.getName()) && !(oldPet.getPetType().equals(pet.getPetType())))
                     || !( oldPet.getName().equals(pet.getName())) && oldPet.getPetType().equals(pet.getPetType())
                     || !( oldPet.getName().equals(pet.getName())) && !(oldPet.getPetType().equals(pet.getPetType()))) {
                    newPets.add(pet);
                }
            }
        }
        return newPets;
    }
    
    // megtalálni az új listában nem szereplő, de a régiben meglévő állatokat
    // össze kéne hasonlítani a nevet-fajtát, mert az id-juk nem fog egyezni
    private Set<PetDTO> findObsoletePets(Set<PetDTO> pets, Set<PetDTO> oldPets) {
        Set<PetDTO> oldPetsToDelete = new HashSet<>();
        for (PetDTO oldPet : oldPets) {
            for (PetDTO pet : pets) {
                if ( !(oldPet.getName().equals(pet.getName()) && oldPet.getPetType().equals(pet.getPetType())) ) {
                    oldPetsToDelete.add(oldPet);
                }
            }
        }
        return oldPetsToDelete;
    }
    
    private Set<PetDTO> findOldPetsAndConvertToDTO(int userId){
        Set<Pet> oldPets = ur.findUser(userId).getOwner().getPets();
        return DTOConversion.convertPetsToPetDTOs(oldPets);
    }

    @Transactional
    public void setWorkingDay(int dayId, Availability avail) {
        ur.setDayAvail(dayId, avail);
    }
    
    @Transactional
    private void removePets(Set<Integer> toRemove) {
        Set<Pet> pets = getCurrentUser().getOwner().getPets();
        for (Pet p : pets) {
            if (toRemove.contains(p.getId())) {
                ur.deletePet(p);
            }
        }
    }

    public List<SitterViewDTO> filterSitters(SearchCriteriaDTO criteria) {
        List<Sitter> sitterUsers = searchResults(criteria.getName(), criteria.getPetType(), criteria.getPlaceOfService(), criteria.getPostCode());
        List<SitterViewDTO> petSitters = new ArrayList<>();
        for (Sitter sitterUser : sitterUsers) {
            SitterViewDTO sitter = DTOConversion.convertToSitterViewDTO(sitterUser.getUser(), sitterUser);
            petSitters.add(sitter);
        }
        return petSitters;
    }

    @Transactional
    public void createUser(UserRegistrationDTO userData) throws AlreadyExistsException {
        if (ur.userAlreadyExists(userData.getEmail())) {
            throw new AlreadyExistsException("Ilyen e-mailcím már létezik az adatbázisban!");
        }
        //Authority auth = ur.findAuthority("ROLE_USER");
        User newUser = new User(userData.getUsername(), userData.getEmail(), pwd.encode(userData.getPassword()));
        //newUser.setAuthorities(auth);
        ur.newUser(newUser);
    }

    @Transactional
    public void suspendAccount() {
        User u = ur.findUser(getCurrentUser().getId());
        u.getAuthorities().clear();
    }

    private SitterService createServiceWithoutPrice(PlaceOfService place, PetType petType) {
        return new SitterService(place, petType);
    }

    private List<Sitter> searchResults(String name, PetType pet, PlaceOfService pl, int postal) {
        List<Sitter> sitters = ur.getAllSitters();
        if (!name.isEmpty()) {
            sitters = filterByName(name, sitters);
        }
        if (pet != null) {
            sitters = filterByPetType(pet, sitters);
        }
        if (pl != null) {
            sitters = filterByPlace(pl, sitters);
        }
        if (postal != 0) {
            sitters = filterByPostal(postal, sitters);
        }
        return sitters;
    }

    private List<Sitter> filterByName(String name, List<Sitter> list) {
        return list.stream().filter(s -> s.getUser().getName().contains(name))
                .collect(Collectors.toList());
    }

    private List<Sitter> filterByPetType(PetType p, List<Sitter> list) {
        return list.stream().filter(s -> s.getPetTypes()
                .contains(p)).collect(Collectors.toList());
    }

    private List<Sitter> filterByPlace(PlaceOfService p, List<Sitter> list) {
        return list.stream().filter(s -> s.getServices().stream()
                .anyMatch(srv -> srv.getPlace() == p)).collect(Collectors.toList());
    }

    private List<Sitter> filterByPostal(int postal, List<Sitter> list) {
        return list.stream().filter(s -> s.getAddress()
                .getPostalCode() == postal).collect(Collectors.toList());
    }

    public User getCurrentUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    @Transactional
    public void saveSitterImage(int sitterId, ImageModel image) {
        Sitter sitter = ur.findSitterById(sitterId);
        imageRepository.saveAndFlush(image);
        sitter.setProfilePhoto(image);
    }

    public int findSitterIdByUserId(int userId) {
        User user = ur.findUser(userId);
        return user.getSitter().getId();
    }
}
