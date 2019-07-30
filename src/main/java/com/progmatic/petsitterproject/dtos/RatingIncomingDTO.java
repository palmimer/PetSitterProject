/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progmatic.petsitterproject.dtos;

/**
 *
 * @author progmatic
 */
public class RatingIncomingDTO {
    private int userId;
    private int newRating;

    public RatingIncomingDTO() {
    }

    public RatingIncomingDTO(int userId, int newRating) {
        this.userId = userId;
        this.newRating = newRating;
    }

    public int getUserId() {
        return userId;
    }

    public int getNewRating() {
        return newRating;
    }
    
    
}
