package com.example.dto;

public class ScoreboardEntry {
    private String countryName;
    private String songTitle;
    private long juryPoints;
    private long publicPoints;
    private long totalPoints;

    // 1. Manual Constructor (Required for the @Query in PointRepository)
    public ScoreboardEntry(String countryName, String songTitle, long juryPoints, long publicPoints, long totalPoints) {
        this.countryName = countryName;
        this.songTitle = songTitle;
        this.juryPoints = juryPoints;
        this.publicPoints = publicPoints;
        this.totalPoints = totalPoints;
    }

    // 2. Manual Getters (Fixes the "cannot find symbol" error)
    public String getSongTitle() {
        return songTitle;
    }

    public String getCountryName() {
        return countryName;
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
}