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
public class RatingResponseDTO {
    private int userId;
    private double averageRating;
    private int numberOfRatings;

    public RatingResponseDTO() {
    }

    public RatingResponseDTO(int userId, double averageRating, int numberOfRatings) {
        this.userId = userId;
        this.averageRating = averageRating;
        this.numberOfRatings = numberOfRatings;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public double getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(double averageRating) {
        this.averageRating = averageRating;
    }

    public int getNumberOfRatings() {
        return numberOfRatings;
    }

    public void setNumberOfRatings(int numberOfRatings) {
        this.numberOfRatings = numberOfRatings;
    }
    
}
