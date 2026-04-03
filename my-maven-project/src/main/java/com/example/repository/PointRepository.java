package com.example.repository;

import com.example.model.Point;
import com.example.model.Voter; 
import com.example.model.Show;  
import com.example.model.Song;
import com.example.dto.ScoreboardEntry; 
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PointRepository extends JpaRepository<Point, Long> {

    // 1. Validation methods (already working)
    boolean existsByVoterAndShowAndSong(Voter voter, Show show, Song song);
    boolean existsByVoterAndShowAndScore(Voter voter, Show show, int score);

    // 2. The "A+" Aggregate Query
    // This calculates Jury vs Public totals in one database trip
    @Query("SELECT new com.example.dto.ScoreboardEntry(" +
           "s.country.name, s.title, " +
           "SUM(CASE WHEN TYPE(p.voter) = com.example.model.Jury THEN p.score ELSE 0 END), " +
           "SUM(CASE WHEN TYPE(p.voter) = com.example.model.Voter THEN p.score ELSE 0 END), " +
           "SUM(p.score)) " +
           "FROM Point p JOIN p.song s " +
           "WHERE p.show.id = :showId " +
           "GROUP BY s.id, s.country.name, s.title " +
           "ORDER BY SUM(p.score) DESC")
    List<ScoreboardEntry> calculateResultsForShow(@Param("showId") Long showId);
}