package com.example.model;

import jakarta.persistence.*;
import java.util.List;
import java.util.ArrayList;

@Entity
@Table(name = "songs")
public class Song {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    // 1. FetchType.LAZY is best practice to keep the app fast
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "country_id", unique = true) 
    private Country country;

    // 2. orphanRemoval ensures that if you delete a song, its performers are cleaned up
    @OneToMany(mappedBy = "song", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Performer> performers = new ArrayList<>();

    // --- RELATIONSHIP: Many-to-Many with Shows ---
    @ManyToMany
    @JoinTable(
        name = "song_shows", 
        joinColumns = @JoinColumn(name = "song_id"), 
        inverseJoinColumns = @JoinColumn(name = "show_id")
    )
    private List<Show> shows = new ArrayList<>();

    // --- NEW FIELD: Votes ---
    @Column(nullable = false)
    private int votes = 0; // Default value is 0

    public Song() {} // Required by JPA

    // --- Getters and Setters ---
    public Long getId() { return id; }
    
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public Country getCountry() { return country; }
    public void setCountry(Country country) { this.country = country; }

    public List<Performer> getPerformers() { return performers; }
    public void setPerformers(List<Performer> performers) { this.performers = performers; }

    public List<Show> getShows() { return shows; }
    public void setShows(List<Show> shows) { this.shows = shows; }

    // --- NEW GETTER AND SETTER FOR VOTES ---
    public int getVotes() {
        return votes;
    }

    public void setVotes(int votes) {
        this.votes = votes;
    }

    // 3. Helper method: Professional way to add performers
    public void addPerformer(Performer performer) {
        performers.add(performer);
        performer.setSong(this);
    }
}