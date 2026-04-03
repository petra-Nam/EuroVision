package com.example.model;

import jakarta.persistence.*;

@Entity
@Table(name = "points")
public class Point {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int score;

    @ManyToOne
    @JoinColumn(name = "voter_id")
    private Voter voter;

    @ManyToOne
    @JoinColumn(name = "song_id")
    private Song song;

    @ManyToOne
    @JoinColumn(name = "show_id")
    private Show show;

    // 1. Required No-Args Constructor
    public Point() {}

    // 2. Required All-Args Constructor
    public Point(int score, Voter voter, Song song, Show show) {
        this.score = score;
        this.voter = voter;
        this.song = song;
        this.show = show;
    }

    // --- MANUAL SETTERS (These fix the 4 errors in your log) ---

    public void setScore(int score) {
        this.score = score;
    }

    public void setVoter(Voter voter) {
        this.voter = voter;
    }

    public void setSong(Song song) {
        this.song = song;
    }

    public void setShow(Show show) {
        this.show = show;
    }

    // --- MANUAL GETTERS (Good practice for the Scoreboard later) ---

    public int getScore() { return score; }
    public Voter getVoter() { return voter; }
    public Song getSong() { return song; }
    public Show getShow() { return show; }
    public Long getId() { return id; }
}