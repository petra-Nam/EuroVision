package com.example.repository;

import com.example.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PointRepository extends JpaRepository<Point, Long> {
    // These names must match the fields in Point.java exactly
    boolean existsByVoterAndShowAndSong(Voter voter, Show show, Song song);
    boolean existsByVoterAndShowAndScore(Voter voter, Show show, int score);
}