package com.CoficabBackEnd.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "announcement")
public class Announcement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String message;
    private String createdBy;
    private LocalDateTime createdAt; // Field for time of creation
    private String tag; // Field for tag
    @Column(length = 1000)
    private String description; // Field for description
    @ManyToOne
    @JoinColumn(name = "formation_id") // Make sure the correct column name is specified
    private Formation formation;

    public Announcement() {
        this.createdAt = LocalDateTime.now();
    }

    public Announcement(String message, String createdBy, Formation formation, String tag, String description) {
        this.message = message;
        this.createdBy = createdBy;
        this.createdAt = LocalDateTime.now();
        this.formation = formation;
        this.tag = tag;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
