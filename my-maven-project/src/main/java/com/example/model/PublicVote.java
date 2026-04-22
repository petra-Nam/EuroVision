package com.example.model;

import jakarta.persistence.*;

@Entity
@Table(name = "public_votes")
public class PublicVote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "voter_id", nullable = false)
    private Voter voter;

    @ManyToOne
    @JoinColumn(name = "song_id", nullable = false)
    private Song song;

    @ManyToOne
    @JoinColumn(name = "show_id", nullable = false)
    private Show show;

    public PublicVote() {}

    public PublicVote(Voter voter, Song song, Show show) {
        this.voter = voter;
        this.song = song;
        this.show = show;
    }

    // --- GETTERS AND SETTERS ---
    public Long getId() { return id; }
    public Voter getVoter() { return voter; }
    public void setVoter(Voter voter) { this.voter = voter; }
    public Song getSong() { return song; }
    public void setSong(Song song) { this.song = song; }
    public Show getShow() { return show; }
    public void setShow(Show show) { this.show = show; }
}