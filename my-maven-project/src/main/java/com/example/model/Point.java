package com.example.model;

import jakarta.persistence.*;

@Entity
@Table(name = "points")
public class Point {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private int score; // 1-12 points assigned by Juries

    @ManyToOne
    @JoinColumn(name = "voter_id")
    private Voter voter; // This will be a Jury member

    @ManyToOne
    @JoinColumn(name = "song_id")
    private Song song;

    @ManyToOne
    @JoinColumn(name = "show_id")
    private Show show;

    public Point() {}

    // Getters and Setters...
    public int getScore() { return score; }
    public void setScore(int score) { this.score = score; }
    public Voter getVoter() { return voter; }
    public void setVoter(Voter voter) { this.voter = voter; }
    public Song getSong() { return song; }
    public void setSong(Song song) { this.song = song; }
    public Show getShow() { return show; }
    public void setShow(Show show) { this.show = show; }
    public Long getId() { return id; }
}