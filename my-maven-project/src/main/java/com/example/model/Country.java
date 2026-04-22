package com.example.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;

@Entity
@Table(name = "countries")
public class Country {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    // In Country.java
@OneToOne(mappedBy = "country")
@JsonIgnore // <--- This ensures Country doesn't try to load the Song again
private Song song;

    private boolean isBigFive;
    private boolean isVirtualGroup;

    // Manual Setters 
    public void setName(String name) { this.name = name; }
    public void setBigFive(boolean bigFive) { this.isBigFive = bigFive; }
    public void setVirtualGroup(boolean virtualGroup) { this.isVirtualGroup = virtualGroup; }

    // Manual Getters 
    public Long getId() { return id; }
    public String getName() { return name; }
    public boolean isBigFive() { return isBigFive; }
    public boolean isVirtualGroup() { return isVirtualGroup; }
    
    public Country() {} // Default constructor for JPA
}