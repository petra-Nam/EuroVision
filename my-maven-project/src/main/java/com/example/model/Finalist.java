package com.example.model;

import jakarta.persistence.*;

@Entity
@Table(name = "finalists")
public class Finalist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "song_id", nullable = false)
    private Song song;

    // To track if they were Big 5 or earned it in a Semi
    private String status; 

    public Finalist() {}

    public Finalist(Song song, String status) {
        this.song = song;
        this.status = status;
    }

    // Standard Getters and Setters
    public Long getId() { return id; }
    public Song getSong() { return song; }
    public void setSong(Song song) { this.song = song; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}