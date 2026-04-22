package com.example.dto;

public class ScoreboardEntry {
    private String countryName;
    private String songTitle;
    private long juryPoints;   // Changed from Long to long
    private long publicPoints; // Changed from Long to long
    private long totalPoints;  // Changed from Long to long

    // 1. Constructor to handle long primitives
    public ScoreboardEntry(String countryName, String songTitle, long juryPoints, long publicPoints, long totalPoints) {
        this.countryName = countryName;
        this.songTitle = songTitle;
        this.juryPoints = juryPoints;
        this.publicPoints = publicPoints;
        this.totalPoints = totalPoints;
    }

    // 2. Standard Getters
    public String getCountryName() {
        return countryName;
    }

    public String getSongTitle() {
        return songTitle;
    }

    public long getJuryPoints() {
        return juryPoints;
    }

    public long getPublicPoints() {
        return publicPoints;
    }

    public long getTotalPoints() {
        return totalPoints;
    }

    // 3. Optional: Setters (
    public void setPublicPoints(long publicPoints) {
        this.publicPoints = publicPoints;
    }

    public void setTotalPoints(long totalPoints) {
        this.totalPoints = totalPoints;
    }
}