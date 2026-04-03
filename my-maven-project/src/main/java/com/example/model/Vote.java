package com.example.model;

import jakarta.persistence.*;

@Entity
@Table(name = "votes")
public class Vote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // The person casting the vote (Your existing Voter/Jury entity)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "voter_id", nullable = false)
    private Voter voter;

    // The song receiving the vote
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "song_id", nullable = false)
    private Song song;

    // The specific show (Semi 1, Semi 2, or Final) [cite: 45]
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "show_id", nullable = false)
    private Show show;

    // The raw rank (1-10) or calculated points (1, 2, 3... 12) 
    @Column(nullable = false)
    private int points;

    public Vote() {}

    // Getters and Setters
    public Long getId() { return id; }

    public Voter getVoter() { return voter; }
    public void setVoter(Voter voter) { this.voter = voter; }

    public Song getSong() { return song; }
    public void setSong(Song song) { this.song = song; }

    public Show getShow() { return show; }
    public void setShow(Show show) { this.show = show; }

    public int getPoints() { return points; }
    public void setPoints(int points) { this.points = points; }
}