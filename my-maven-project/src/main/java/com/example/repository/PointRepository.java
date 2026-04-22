package com.example.repository;

import com.example.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PointRepository extends JpaRepository<Point, Long> {
    // These names must match the fields in Point.java exactly
    boolean existsByVoterAndShowAndSong(Voter voter, Show show, Song song);
    boolean existsByVoterAndShowAndScore(Voter voter, Show show, int score);

    // Query to sum scores by song and show
    @Query("SELECT COALESCE(SUM(p.score), 0) FROM Point p WHERE p.song = :song AND p.show = :show")
    long sumScoreBySongAndShow(@Param("song") Song song, @Param("show") Show show);
}