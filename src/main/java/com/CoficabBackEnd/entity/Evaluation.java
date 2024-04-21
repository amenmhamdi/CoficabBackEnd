package com.CoficabBackEnd.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "evaluation")
public class Evaluation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int rating;
    private int response1;
    private int response2;
    private int response3;
    private int response4;
    // New status field
    private String status;
	private String createdBy;

 
    @ManyToOne
    @JoinColumn(name = "formation_id")
    private Formation formation;

    



    public int getResponse1() {
        return response1;
    }

    public void setResponse1(int response1) {
        this.response1 = response1;
    }

    public int getResponse2() {
        return response2;
    }

    public void setResponse2(int response2) {
        this.response2 = response2;
    }

    public int getResponse3() {
        return response3;
    }

    public void setResponse3(int response3) {
        this.response3 = response3;
    }

    public int getResponse4() {
        return response4;
    }

    public void setResponse4(int response4) {
        this.response4 = response4;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

 
    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    // Getters and setters for status field
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

   

    public Formation getFormation() {
        return formation;
    }

    public void setFormation(Formation formation) {
        this.formation = formation;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    
}
