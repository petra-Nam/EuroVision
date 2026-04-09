package com.example.model;

import jakarta.persistence.*;

@Entity
@Table(name = "points")
public class Point {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int score; // Changed from pointsValue to score to match your Query

    @ManyToOne
    @JoinColumn(name = "voter_id")
    private Voter voter;

    @ManyToOne
    @JoinColumn(name = "song_id")
    private Song song;

    @ManyToOne
    @JoinColumn(name = "show_id")
    private Show show; // Added this relationship

    public Point() {}

    // --- GETTERS AND SETTERS ---
    public int getScore() { return score; }
    public void setScore(int score) { this.score = score; }

    public Show getShow() { return show; }
    public void setShow(Show show) { this.show = show; }

    public Voter getVoter() { return voter; }
    public void setVoter(Voter voter) { this.voter = voter; }

    public Song getSong() { return song; }
    public void setSong(Song song) { this.song = song; }
    
    public Long getId() { return id; }
}